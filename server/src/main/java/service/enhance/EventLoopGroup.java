package service.enhance;

import service.enhance.handlers.Handler;

import java.io.IOException;
import java.nio.channels.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 10:02
 */
public class EventLoopGroup {
    private EventLoop[] eventLoops;

    private Integer size;

    private final AtomicInteger idx = new AtomicInteger();

    private boolean mainLoop = false;

    public EventLoopGroup(boolean mainLoop, int size) {
        if (size == 0) {
            return;
        }
        this.size = size;
        this.mainLoop = mainLoop;
        if (mainLoop) {
            eventLoops = new MainEventLoop[size];
            for (int i = 0; i < size; i++) {
                eventLoops[i] = new MainEventLoop();
            }
        } else {
            eventLoops = new SubEventLoop[size];
            for (int i = 0; i < size; i++) {
                eventLoops[i] = new SubEventLoop();
            }
        }
    }

    /**
     * 轮询
     *
     * @return
     */
    public EventLoop next() {
        EventLoop eventLoop = this.eventLoops[Math.abs(this.idx.getAndIncrement() % size)];
        if (!eventLoop.isAlive()) {
            eventLoop.startLoop();
            EnhanceContext.addSelector(eventLoop.selector);
        }
        return eventLoop;
    }

    /**
     * 理论上只需要注册acceptable事件
     *
     * @param serverSocketChannel
     */
    public void registerChannel(SelectableChannel serverSocketChannel) {
        if (mainLoop) {
            SelectionKey selectionKey = this.next().registerChannel(serverSocketChannel, SelectionKey.OP_ACCEPT);
            EnhanceContext.addServerSelectKey(selectionKey);
        } else {
            SelectionKey selectionKey = this.next().registerChannel(serverSocketChannel, SelectionKey.OP_READ);
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            try {
                System.out.println(Thread.currentThread().getName() + "收到新连接 : " + channel.getRemoteAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addFirstHandler(Handler handler) {
        for (EventLoop eventLoop : eventLoops) {
            eventLoop.addFirstHandler(handler);
        }
    }
}
