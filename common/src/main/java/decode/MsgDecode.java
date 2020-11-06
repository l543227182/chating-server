package decode;


import domain.ClientAction;
import domain.MsgObj;

import java.util.Objects;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 9:40
 */
public class MsgDecode {

    public static MsgObj decodeMsg(String content) {
        if (Objects.nonNull(content)) {

            String[] msgPiece = content.split("#");
            if (msgPiece.length == 3) {
                String user = msgPiece[0];
                String action = msgPiece[1];
                String userMsg = msgPiece[2];
                return new MsgObj(user, ClientAction.valueOf(action), userMsg);
            } else if (msgPiece.length == 2) {
                String user = msgPiece[0];
                String action = msgPiece[1];
                return new MsgObj(user, ClientAction.valueOf(action), null);
            }
            throw new RuntimeException("不可解析的信息");
        }
        return new MsgObj();
    }
}
