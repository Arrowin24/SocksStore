package ru.arrowin.socksstore.services;


import ru.arrowin.socksstore.model.Socks;
import ru.arrowin.socksstore.model.SocksConsignment;
import ru.arrowin.socksstore.model.SocksOrder;

public interface SocksService {

    int addSocks(SocksConsignment consignment);

    int deleteSocks(SocksConsignment consignment);

    String messageOfResidual(Socks socks);

    int getSocksQuantityByParams(String color, int size, int cottonMin, int cottonMax);
}
