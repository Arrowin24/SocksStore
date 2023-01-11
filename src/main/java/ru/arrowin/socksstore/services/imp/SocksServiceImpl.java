package ru.arrowin.socksstore.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.arrowin.socksstore.model.Socks;
import ru.arrowin.socksstore.model.SocksConsignment;
import ru.arrowin.socksstore.services.FilesService;
import ru.arrowin.socksstore.services.SocksService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


@Service
public class SocksServiceImpl implements SocksService {

    private final HashMap<Socks, Integer> socksStore = new HashMap<>();

    private final FilesService filesService;

    public SocksServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        uploadSocksStoreFromFile();
    }

    @Override
    public int addSocks(SocksConsignment consignment) {
        Socks socks = consignment.getSocks();
        int quantity = consignment.getQuantity();
        socksStore.put(socks, socksStore.getOrDefault(socks, 0) + quantity);
        saveToFile();
        return socksStore.get(socks);
    }

    @Override
    public int deleteSocks(SocksConsignment consignment) {
        Socks socks = consignment.getSocks();
        int quantity = consignment.getQuantity();
        if (isEnoughSocks(consignment)) {
            socksStore.put(socks, socksStore.get(socks) - quantity);
            saveToFile();
            return socksStore.get(socks);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String messageOfResidual(Socks socks) {
        return socks.toString() + "\n" + "На складе: " + socksStore.get(socks) + " шт.";
    }

    @Override
    public int getSocksQuantityByParams(String color, int size, int cottonMin, int cottonMax) {
        int quantity = 0;
        paramsValidation(color, size, cottonMax);
        paramsValidation(color, size, cottonMin);
        for (Socks socks : socksStore.keySet()) {
            if (equalsColor(color, socks) && equalsSize(size, socks) && isCottonPartBetween(cottonMin, cottonMax,
                                                                                            socks))
                quantity += socksStore.get(socks);
        }
        return quantity;
    }

    @Override
    public void uploadSocksStoreFromFile() {
        try {
            String json = filesService.readSocksStoreFromFile();
            if (!json.isBlank()) {
                List<SocksConsignment> list = new ObjectMapper().readValue(json,
                                                                           new TypeReference<LinkedList<SocksConsignment>>() {
                                                                           });
                convertListToSocksStoreMap(list);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void saveToFile() {
        try {
            List<SocksConsignment> listOfSocks = convertMapToListSocks();
            String json = new ObjectMapper().writeValueAsString(listOfSocks);
            filesService.saveSocksStorageToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<SocksConsignment> convertMapToListSocks() {
        List<SocksConsignment> list = new LinkedList<>();
        for (Socks socks : socksStore.keySet()) {
            list.add(new SocksConsignment(socks, socksStore.get(socks)));
        }
        return list;
    }

    private void convertListToSocksStoreMap(List<SocksConsignment> consignments) {
        for (SocksConsignment consignment : consignments) {
            socksStore.put(consignment.getSocks(), consignment.getQuantity());
        }
    }

    //Так как все проверки уже настроены в конструкторе, то при неправильном вызове конструктора появится
    // IllegalArgumentException()
    public void paramsValidation(String color, int size, int cottonPart) throws IllegalArgumentException {
        new Socks(color, size, cottonPart);
    }

    private boolean equalsColor(String color, Socks socks) {
        return Socks.Color.valueOf(color.toUpperCase()).equals(socks.getColor());
    }

    private boolean isCottonPartBetween(int cottonMin, int cottonMax, Socks socks) {
        return socks.getCottonPart() >= cottonMin && socks.getCottonPart() <= cottonMax;
    }

    private boolean equalsSize(int size, Socks socks) {
        return socks.getSize().getSize() == size;
    }

    private boolean isEnoughSocks(SocksConsignment consignment) {
        Socks socks = consignment.getSocks();
        int quantity = consignment.getQuantity();
        if (!socksStore.containsKey(socks)) {
            return false;
        }
        return socksStore.get(socks) >= quantity;
    }

}
