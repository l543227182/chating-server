package service.enhance;

import service.enhance.handlers.in.EncodeMsgHandler;
import service.enhance.handlers.in.EventHandler;
import service.enhance.handlers.in.ReadMsgHandler;
import service.enhance.handlers.out.WriteMsgHandler;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 16:14
 */
public class SubEventLoop extends EventLoop {

    @Override
    public void initHandler() {
        this.addHandler(new ReadMsgHandler());
        this.addHandler(new EncodeMsgHandler());
        this.addHandler(new EventHandler());
        this.addHandler(new WriteMsgHandler());
    }
}
