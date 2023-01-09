package ru.arrowin.socksstore.services.imp;

import org.springframework.stereotype.Service;
import ru.arrowin.socksstore.model.Socks;
import ru.arrowin.socksstore.model.SocksOrder;
import ru.arrowin.socksstore.services.SocksService;

import java.util.HashMap;

@Service
public class SocksServiceImpl implements SocksService {
    private HashMap<Socks, Integer> socksStore = new HashMap<>();


    @Override
    public int addSocks(SocksOrder order) {
        Socks socks = order.getSocks();
        int quantity = order.getQuantity();
        socksStore.put(socks, socksStore.getOrDefault(socks, 0) + quantity);
        return socksStore.get(socks);
    }

    @Override
    public int deleteSocks(SocksOrder order) {
        Socks socks = order.getSocks();
        int quantity = order.getQuantity();
        if (isEnoughSocks(order)) {
            socksStore.put(socks, socksStore.get(socks) - quantity);
            return socksStore.get(socks);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String messageOfResidual(Socks socks) {
        String message = socks.toString() + "\n" + "На складе: " + socksStore.get(socks) + " шт.";
        return message;
    }

    private boolean isEnoughSocks(SocksOrder order) {
        Socks socks = order.getSocks();
        int quantity = order.getQuantity();
        if (!socksStore.containsKey(socks)) {
            return false;
        }
        return socksStore.get(socks) >= quantity;
    }

}
