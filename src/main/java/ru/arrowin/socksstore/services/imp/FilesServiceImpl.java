package ru.arrowin.socksstore.services.imp;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.arrowin.socksstore.exceptions.CreateFileException;
import ru.arrowin.socksstore.exceptions.ReadFileException;
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
    private void init() throws CreateFileException {
        try {
            if (!Files.exists(Path.of(dataFilePath, ordersFileName))) {
                Files.createFile(Path.of(dataFilePath, ordersFileName));
            }
            if (!Files.exists(Path.of(dataFilePath, socksStoreFileName))) {
                Files.createFile(Path.of(dataFilePath, socksStoreFileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CreateFileException(ordersFileName + " or " + socksStoreFileName);
        }
    }

    @Override
    public String readOrdersFromFile() throws ReadFileException {
        try {
            return Files.readString(Path.of(dataFilePath, ordersFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadFileException(ordersFileName);
        }
    }

    @Override
    public String readSocksStoreFromFile() throws ReadFileException {
        try {
            return Files.readString(Path.of(dataFilePath, socksStoreFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadFileException(socksStoreFileName);
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
        } catch (CreateFileException e) {
            throw new RuntimeException(e);
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
        } catch (CreateFileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getOrdersDataFile() {
        return new File(dataFilePath + "/" + ordersFileName);
    }

    @Override
    public File getSocksStoreDataFile() {
        return new File(dataFilePath + "/" + socksStoreFileName);
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
    public boolean uploadSocksStoreFile(MultipartFile fromFile) {
        File socksStoreDataFile = getSocksStoreDataFile();
        if (!socksStoreDataFile.exists()) {
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(socksStoreDataFile)) {
            IOUtils.copy(fromFile.getInputStream(), fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Path createTempFile(String suffix) throws CreateFileException {
        try {
            Path file = Files.createTempFile(Path.of(dataFilePath), "tempFile", suffix);
            file.toFile().deleteOnExit();
            return file;
        } catch (IOException e) {
            throw new CreateFileException("Temp files");
        }
    }

    @Override
    public File downloadSocksStoreFile() {
        File file = new File(dataFilePath + "/" + socksStoreFileName);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    @Override
    public File downloadOrdersFile() {
        File file = new File(dataFilePath + "/" + ordersFileName);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    private void cleanFile(String fileName) throws CreateFileException {
        try {
            Files.deleteIfExists(Path.of(dataFilePath, fileName));
            Files.createFile(Path.of(dataFilePath, fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CreateFileException(e.getMessage());
        }
    }

}
