package ru.arrowin.socksstore.services;


import org.springframework.stereotype.Service;
import ru.arrowin.socksstore.exceptions.ReadFileException;
import ru.arrowin.socksstore.model.Socks;
import ru.arrowin.socksstore.model.SocksConsignment;


public interface SocksService {

    int addSocks(SocksConsignment consignment);

    int deleteSocks(SocksConsignment consignment);

    String messageOfResidual(Socks socks);

    int getSocksQuantityByParams(String color, int size, int cottonMin, int cottonMax);

    void uploadSocksStoreFromFile() throws ReadFileException;
}
