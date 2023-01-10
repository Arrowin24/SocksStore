package ru.arrowin.socksstore.services.imp;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.arrowin.socksstore.services.FilesService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${path.to.data.file}") private String dataFilePath;
    @Value("${name.of.order.file}") private String ordersFileName;

    @Value("${name.of.socks.store.file}") private String socksStoreFileName;

    @PostConstruct
    private void init() {
        try {
            if (!Files.exists(Path.of(dataFilePath, ordersFileName))) {
                Files.createFile(Path.of(dataFilePath, ordersFileName));
            }
            if (!Files.exists(Path.of(dataFilePath, socksStoreFileName))) {
                Files.createFile(Path.of(dataFilePath, socksStoreFileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readOrdersFromFile() {
        try {
            return Files.readString(Path.of(dataFilePath, ordersFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readSocksStoreFromFile() {
        try {
            return Files.readString(Path.of(dataFilePath, socksStoreFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean saveOrdersToFile(String json) {
        try {
            cleanFile(ordersFileName);
            Files.writeString(Path.of(dataFilePath, ordersFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean saveSocksStorageToFile(String json) {
        try {
            cleanFile(socksStoreFileName);
            Files.writeString(Path.of(dataFilePath, socksStoreFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File getOrdersDataFile() {
        return new File(dataFilePath + "/" + ordersFileName);
    }

    @Override
    public boolean uploadOrdersFile(MultipartFile fromFile) {
        File ordersFile = getOrdersDataFile();
        if (!ordersFile.exists()) {
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(ordersFile)) {
            IOUtils.copy(fromFile.getInputStream(), fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Path createTempFile(String suffix) {
        try {
            Path file = Files.createTempFile(Path.of(dataFilePath), "tempFile", suffix);
            file.toFile().deleteOnExit();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanFile(String fileName) {
        try {
            Files.deleteIfExists(Path.of(dataFilePath, fileName));
            Files.createFile(Path.of(dataFilePath, fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
