package service.enhance.handlers.out;

import com.google.gson.Gson;
import domain.ClientAction;
import domain.MsgObj;
import encode.MsgEncode;
import service.enhance.EnhanceContext;
import service.enhance.handlers.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-06 10:32
 */
public class WriteMsgHandler implements Handler {
    @Override
    public Object handler(SelectionKey selectionKey, Object handleObj) {
        if (selectionKey.isWritable()) {
            Object attachment = selectionKey.attachment();
            selectionKey.attach("");
            if (Objects.nonNull(attachment) && attachment instanceof MsgObj) {
                try {
                    MsgObj msg = (MsgObj) attachment;
                    ClientAction serverAction = msg.getClientAction();
                    Gson gson = new Gson();
                    if (ClientAction.REFRESH_USER.equals(serverAction)) {
                        msg.setMsg(gson.toJson(EnhanceContext.users));
                    }
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    channel.write(ByteBuffer.wrap(MsgEncode.encodeMsg(msg).getBytes()));
                    selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
