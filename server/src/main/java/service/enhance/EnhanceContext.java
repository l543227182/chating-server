package service.enhance;

import domain.MsgObj;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * TODO
 *
 * @author luochao
 * @since 2020-11-05 17:46
 */
public class EnhanceContext {
    private final static List<Selector> allSelectors = new Vector<>();
    private final static List<SelectionKey> serverSelect = new Vector<>();

    public final static List<String> users = new Vector<String>();

    public static void addSelector(Selector selector) {
        allSelectors.add(selector);
    }

    public static void addServerSelectKey(SelectionKey selectionKey) {
        serverSelect.add(selectionKey);
    }


    public static Set<SelectionKey> fetchCurrentClientSelectKey() {
        Set<SelectionKey> selectionKeySets = new HashSet<>();
        for (Selector allSelector : allSelectors) {
            Set<SelectionKey> selectionKeys = allSelector.keys();
            selectionKeys.forEach(item -> {
                if (!serverSelect.contains(item)) {
                    selectionKeySets.add(item);
                }
            });
        }
        return selectionKeySets;
    }

    public static void sendAllMessage(MsgObj msgObj) {
        Set<SelectionKey> selectionKeys = fetchCurrentClientSelectKey();
        selectionKeys.stream().forEach(item -> {
            if (item.isValid()) {
                item.attach(msgObj);
                item.interestOps(item.interestOps() | SelectionKey.OP_WRITE);
            }
        });
    }
}
