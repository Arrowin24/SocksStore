package ru.arrowin.socksstore.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.arrowin.socksstore.exceptions.CreateFileException;
import ru.arrowin.socksstore.exceptions.ReadFileException;

import java.io.File;
import java.nio.file.Path;

public interface FilesService {
    String readOrdersFromFile() throws ReadFileException;

    String readSocksStoreFromFile() throws ReadFileException;

    boolean saveOrdersToFile(String json);

    boolean saveSocksStorageToFile(String json);

    File getOrdersDataFile();

    File getSocksStoreDataFile();

    boolean uploadOrdersFile(MultipartFile fromFile);

    boolean uploadSocksStoreFile(MultipartFile fromFile);

    Path createTempFile(String suffix) throws CreateFileException;

    File downloadSocksStoreFile();

    File downloadOrdersFile();
}
