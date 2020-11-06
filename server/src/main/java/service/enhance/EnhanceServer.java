package service.enhance;

import service.enhance.handlers.Handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 10:03
 */
public class EnhanceServer {
    private ServerSocketChannel serverSocketChannel;

    EventLoopGroup boss = new EventLoopGroup(true, 1);
    EventLoopGroup work = new EventLoopGroup(false, 1);

    public static void main(String[] args) {
        EnhanceServer enhanceServer = new EnhanceServer();
    }

    public EnhanceServer() {
        boss.addFirstHandler(new Handler() {
            @Override
            public Object handler(SelectionKey selectionKey, Object handleObj) {
                if (selectionKey.isAcceptable()) {
                    try {
                        ServerSocketChannel ch = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = ch.accept();
                        socketChannel.configureBlocking(false);
                        work.registerChannel(socketChannel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });

        try {
            this.initAndRegister();
            this.bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initAndRegister() throws Exception {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        boss.registerChannel(serverSocketChannel);
    }

    private void bind() throws IOException {
        int port = 8899;
        String setPort = System.getProperty("port");
        if (Objects.nonNull(setPort)) {
            port = Integer.valueOf(setPort);
        }
        //  1、 正式绑定端口，对外服务
        serverSocketChannel.bind(new InetSocketAddress(port));
        System.out.println("启动完成，端口" + port);
    }
}
