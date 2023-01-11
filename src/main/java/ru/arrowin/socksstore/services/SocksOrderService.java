package ru.arrowin.socksstore.services;

import org.springframework.stereotype.Service;
import ru.arrowin.socksstore.model.SocksConsignment;
import ru.arrowin.socksstore.model.SocksOrder;

public interface SocksOrderService {
    void addOrder(SocksConsignment consignment, SocksOrder.Type type);

    void uploadOrdersFromFile();
}
