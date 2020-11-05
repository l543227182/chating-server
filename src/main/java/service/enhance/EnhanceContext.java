package service.enhance;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 17:46
 */
public class EnhanceContext {
    public final static List<Selector> allSelectors = new Vector<>();

    public static void addSelector(Selector selector) {
        allSelectors.add(selector);
    }

    public static Set<SelectionKey> fetchCurrentSelectKey() {
        Set<SelectionKey> selectionKeySets = new HashSet<>();
        for (Selector allSelector : allSelectors) {
            Set<SelectionKey> selectionKeys = allSelector.selectedKeys();
            selectionKeySets.addAll(selectionKeys);
        }
        return selectionKeySets;
    }
}
