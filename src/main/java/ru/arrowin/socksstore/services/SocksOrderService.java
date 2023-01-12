package ru.arrowin.socksstore.services;


import ru.arrowin.socksstore.model.SocksConsignment;

import ru.arrowin.socksstore.model.enums.Type;

public interface SocksOrderService {
    void addOrder(SocksConsignment consignment, Type type);

    void uploadOrdersFromFile();
}
