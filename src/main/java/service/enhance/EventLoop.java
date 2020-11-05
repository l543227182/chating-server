package service.enhance;

import service.enhance.handlers.Handler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 9:43
 */
public abstract class EventLoop extends Thread {
    protected Selector selector;
    protected final Queue<Runnable> tailTasks = new LinkedBlockingQueue(1024);
    public volatile boolean running = false;
    private List<Handler> handlers;

    public EventLoop() {
        try {
            this.selector = Selector.open();
            this.handlers = new LinkedList();
            this.initHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            Runnable task;
            while ((task = tailTasks.poll()) != null) {
                task.run();
            }
            try {
                int select = this.selector.select(1000 * 3);
                if (select > 0) {
                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        try {
                            handler(selectionKey);
                            iterator.remove();
                        } catch (Exception e) {
                            e.printStackTrace();
                            selectionKey.cancel();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startLoop() {
        this.running = true;
        this.start();
    }

    public void handler(SelectionKey selectionKey) {
        if (Objects.nonNull(handlers)) {
            Iterator<Handler> next = handlers.iterator();
            Object result = null;
            while (next.hasNext()) {
                Handler handler = next.next();
                result = handler.handler(selectionKey, result);
            }
        }
    }

    public abstract void initHandler();

    public void addHandler(Handler handler) {
        this.handlers.add(handler);
    }

    public void registerChannel(SelectableChannel selectableChannel, int ops) {
        FutureTask<SelectionKey> futureTask = new FutureTask<>(() -> selectableChannel.register(selector, ops, selectableChannel));
        this.tailTasks.add(futureTask);
        try {
            futureTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
