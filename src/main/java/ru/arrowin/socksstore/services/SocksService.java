package ru.arrowin.socksstore.services;


import ru.arrowin.socksstore.model.Socks;
import ru.arrowin.socksstore.model.SocksOrder;

public interface SocksService {

    int addSocks(SocksOrder order);

    int deleteSocks(SocksOrder order);

    String messageOfResidual(Socks socks);

    int getSocksQuantityByParams(String color, int size, int cottonMin, int cottonMax);
}
