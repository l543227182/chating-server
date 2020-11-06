package service.enhance.handlers.in;

import domain.ClientAction;
import domain.MsgObj;
import service.enhance.EnhanceContext;
import service.enhance.handlers.Handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-06 9:27
 */
public class EventHandler implements Handler {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object handler(SelectionKey selectionKey, Object handleObj) {
        if (selectionKey.isReadable() && handleObj instanceof MsgObj) {
            MsgObj msgObj = (MsgObj) handleObj;
            String user = msgObj.getUser();
            if (Objects.nonNull(msgObj.getClientAction())) {
                switch (msgObj.getClientAction()) {
                    case ON_LINE:
                        EnhanceContext.users.add(user);
                        MsgObj sendMsg = new MsgObj();
                        sendMsg.setUser(msgObj.getUser());
                        sendMsg.setClientAction(ClientAction.REFRESH_USER);
                        EnhanceContext.sendAllMessage(sendMsg);
                        break;
                    case BYE_BYE:
                        try {
                            selectionKey.cancel();
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            channel.socket().close();
                            channel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        EnhanceContext.users.remove(user);
                        MsgObj removeMsg = new MsgObj();
                        removeMsg.setUser(msgObj.getUser());
                        removeMsg.setClientAction(ClientAction.REFRESH_USER);
                        EnhanceContext.sendAllMessage(removeMsg);
                        break;
                    case SAY_SOMETHING:
                        printInfo("(" + user + ")说：" + msgObj.getMsg());
                        String dateTime = sdf.format(new Date());
                        String smsg = user + " " + dateTime + "\n  " + msgObj.getMsg() + "\n";
                        MsgObj newMsg = new MsgObj();
                        newMsg.setUser(msgObj.getUser());
                        newMsg.setClientAction(ClientAction.CHATTING_MSG);
                        newMsg.setMsg(smsg);
                        EnhanceContext.sendAllMessage(newMsg);
                        break;
                }
            }
        }
        return null;
    }

    private void printInfo(String str) {
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }
}
