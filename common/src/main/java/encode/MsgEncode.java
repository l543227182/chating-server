package encode;

import domain.MsgObj;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 9:40
 */
public class MsgEncode {
    private final static String SPLIT = "#";

    public static String encodeMsg(MsgObj msgObj) {
        List<String> msg = new ArrayList<>(4);
        if (StringUtils.isNotEmpty(msgObj.getUser())) {
            msg.add(msgObj.getUser());
        }
        if (Objects.nonNull(msgObj.getClientAction())) {
            msg.add(msgObj.getClientAction().name());
        }
        if (StringUtils.isNotEmpty(msgObj.getMsg())) {
            msg.add(msgObj.getMsg());
        }
        return StringUtils.join(msg, SPLIT);
    }
}
