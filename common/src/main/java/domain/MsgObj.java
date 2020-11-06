package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 9:51
 */
@Data
@NoArgsConstructor
public class MsgObj {
    private String user;
    private ClientAction clientAction;
    private String msg;

    public MsgObj(String user, ClientAction clientAction, String msg) {
        this.user = user;
        this.clientAction = clientAction;
        this.msg = msg;
    }

}
