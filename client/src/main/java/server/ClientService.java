package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class ClientService {
    private static String HOST = "127.0.0.1";
    private static int PORT = 8899;
    private static SocketChannel sc;

    private static Object lock = new Object();

    private static ClientService service;

    public static ClientService getInstance() {
        synchronized (lock) {
            if (service == null) {
                try {
                    service = new ClientService();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return service;
        }
    }

    private ClientService() throws IOException {
        sc = SocketChannel.open();
        sc.configureBlocking(false);

        String setPort = System.getProperty("port");
        if (Objects.nonNull(setPort)) {
            PORT = Integer.valueOf(setPort);
        }

        String setHost = System.getProperty("host");
        if (Objects.nonNull(setHost)) {
            HOST = setHost;
        }
        System.out.println(HOST);
        System.out.println(PORT);
        sc.connect(new InetSocketAddress(HOST, PORT));
    }

    public void sendMsg(String msg) {
        try {
            while (!sc.finishConnect()) {
            }
            sc.write(ByteBuffer.wrap(msg.getBytes("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMsg() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        StringBuffer sb = new StringBuffer();
        int count = 0;
        String msg = null;
        try {
            while ((count = sc.read(buffer)) > 0) {
                sb.append(new String(buffer.array(), 0, count,"UTF-8"));
            }
            if (sb.length() > 0) {
                msg = sb.toString();
                if ("close".equals(sb.toString())) {
                    msg = null;
                    sc.close();
                    sc.socket().close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

}
