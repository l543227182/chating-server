package service.enhance.handlers;

import java.nio.channels.SelectionKey;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 13:50
 */
public interface Handler<T> {
    int inStream = 1;
    Object handler(SelectionKey selectionKey, T handleObj);
}
