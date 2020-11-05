package service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 9:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgObj {
    private String user;
    private ClientAction clientAction;
    private String msg;
}
