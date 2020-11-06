package service.enhance;

import service.enhance.handlers.out.WriteMsgHandler;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.FutureTask;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 13:46
 */
public class MainEventLoop extends EventLoop {

    @Override
    public void initHandler() {

    }
}
