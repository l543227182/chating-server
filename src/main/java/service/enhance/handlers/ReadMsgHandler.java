package service.enhance.handlers;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 17:52
 */
public class ReadMsgHandler implements Handler {
    @Override
    public Object handler(SelectionKey selectionKey, Object handleObj) {
        if (selectionKey.isAcceptable()) {
            try {
                SocketChannel ch = (SocketChannel) selectionKey.channel();
                ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                while (ch.isOpen() && ch.read(requestBuffer) != -1) {
                    // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                    if (requestBuffer.position() > 0) {
                        break;
                    }
                }
                if (requestBuffer.position() == 0) {
                    return null; // 如果没数据了, 则不继续后面的处理
                }
                requestBuffer.flip();
                byte[] content = new byte[requestBuffer.limit()];
                requestBuffer.get(content);
                return new String(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
