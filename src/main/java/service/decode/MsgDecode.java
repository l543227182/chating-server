package service.decode;

import service.domain.ClientAction;
import service.domain.MsgObj;

import java.util.Objects;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 9:40
 */
public class MsgDecode {

    public MsgObj decodeMsg(String content) {
        if (Objects.isNull(content)) {

            String[] msgPiece = content.split("#");
            if (msgPiece.length == 3) {
                String user = msgPiece[0];
                String action = msgPiece[1];
                String userMsg = msgPiece[3];
                return new MsgObj(user, ClientAction.valueOf(action), userMsg);
            } else if (msgPiece.length == 2) {
                String user = msgPiece[0];
                String action = msgPiece[1];
                return new MsgObj(user, ClientAction.valueOf(action), null);
            }
        }
        return new MsgObj();
    }
}
