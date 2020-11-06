package service.enhance.handlers.in;


import decode.MsgDecode;
import domain.MsgObj;
import service.enhance.handlers.Handler;

import java.nio.channels.SelectionKey;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 19:09
 */
public class EncodeMsgHandler implements Handler {

    @Override
    public Object handler(SelectionKey selectionKey, Object handleObj) {
        if(selectionKey.isReadable()) {
            if(handleObj instanceof  String) {
               String msg = (String) handleObj;
                MsgObj msgObj = MsgDecode.decodeMsg(msg);
                return msgObj;
            }
        }
        return null;
    }
}
