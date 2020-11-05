package service.enhance;

import java.nio.channels.SelectionKey;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 13:50
 */
public abstract class Handler {
    public Handler next;
    public Handler prev;

    abstract void handler(SelectionKey selectionKey);
}
