package service.domain;

import lombok.Data;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 9:51
 */
@Data
public class MsgObj {
    private String msg;
    private Integer state;
    private Integer close;
    private String user;
    private String ip;
    private ClientAction clientAction;
}
