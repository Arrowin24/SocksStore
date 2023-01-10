package ru.arrowin.socksstore.services.imp;

import org.springframework.stereotype.Service;
import ru.arrowin.socksstore.model.Socks;
import ru.arrowin.socksstore.model.SocksOrder;
import ru.arrowin.socksstore.services.SocksService;

import java.util.HashMap;

@Service
public class SocksServiceImpl implements SocksService {
    private final HashMap<Socks, Integer> socksStore = new HashMap<>();


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

    @Override
    public int getSocksQuantityByParams(String color, int size, int cottonMin, int cottonMax) {
        int quantity = 0;
        paramsValidation(color,size,cottonMax);
        paramsValidation(color,size,cottonMin);
        for (Socks socks : socksStore.keySet()) {
            if (equalsColor(color, socks) && equalsSize(size, socks) && isCottonPartBetween(cottonMin, cottonMax,
                                                                                            socks))
                quantity += socksStore.get(socks);
        }
        return quantity;
    }

    //Так как все проверки уже настроены в конструкторе, то при неправильном вызове конструктора появится
    // IllegalArgumentException()
    public void paramsValidation(String color, int size, int cottonPart) throws IllegalArgumentException {
        new Socks(color, size, cottonPart);
    }

    private boolean equalsColor(String color, Socks socks) {
        return Socks.Color.valueOf(color.toUpperCase()).equals(socks.getColor());
    }

    private boolean isCottonPartBetween(int cottonMin, int cottonMax, Socks socks) {
        return socks.getCottonPart() >= cottonMin && socks.getCottonPart() <= cottonMax;
    }

    private boolean equalsSize(int size, Socks socks) {
        return socks.getSize().getSize() == size;
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
