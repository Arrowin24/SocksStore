package ru.arrowin.socksstore.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface FilesService {
    String readOrdersFromFile();

    String readSocksStoreFromFile();

    boolean saveOrdersToFile(String json);

    boolean saveSocksStorageToFile(String json);

    File getOrdersDataFile();

    boolean uploadOrdersFile(MultipartFile fromFile);

    Path createTempFile(String suffix);
}
