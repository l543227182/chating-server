package service.enhance;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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

    public EventLoopGroup(boolean mainLoop, int size) {
        if (size == 0) {
            return;
        }
        this.size = size;
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
    public void registerChannel(ServerSocketChannel serverSocketChannel) {
        this.next().registerChannel(serverSocketChannel, SelectionKey.OP_ACCEPT);
    }
}
