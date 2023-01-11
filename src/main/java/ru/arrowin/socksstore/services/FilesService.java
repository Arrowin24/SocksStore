package ru.arrowin.socksstore.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface FilesService {
    String readOrdersFromFile();

    String readSocksStoreFromFile();

    boolean saveOrdersToFile(String json);

    boolean saveSocksStorageToFile(String json);

    File getOrdersDataFile();

    File getSocksStoreDataFile();

    boolean uploadOrdersFile(MultipartFile fromFile);

    boolean uploadSocksStoreFile(MultipartFile fromFile);

    Path createTempFile(String suffix);

    File downloadSocksStoreFile();

    File downloadOrdersFile();
}
