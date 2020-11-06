package server;

import domain.ClientAction;
import domain.MsgObj;
import encode.MsgEncode;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-06 13:39
 */
public class MsgService {

    public static String openMsg(String user) {
        MsgObj msgObj = new MsgObj();
        msgObj.setClientAction(ClientAction.ON_LINE);
        msgObj.setUser(user);
        return MsgEncode.encodeMsg(msgObj);
    }

    public static String sayBye(String user) {
        MsgObj msgObj = new MsgObj();
        msgObj.setClientAction(ClientAction.BYE_BYE);
        msgObj.setUser(user);
        return MsgEncode.encodeMsg(msgObj);
    }

    public static String saySomething(String user, String msg) {
        MsgObj msgObj = new MsgObj();
        msgObj.setClientAction(ClientAction.SAY_SOMETHING);
        msgObj.setMsg(msg);
        msgObj.setUser(user);
        return MsgEncode.encodeMsg(msgObj);
    }
}
