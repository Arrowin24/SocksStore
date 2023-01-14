package ru.arrowin.socksstore.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import ru.arrowin.socksstore.exceptions.ReadFileException;
import ru.arrowin.socksstore.exceptions.SaveFileException;
import ru.arrowin.socksstore.model.SocksConsignment;
import ru.arrowin.socksstore.model.SocksOrder;
import ru.arrowin.socksstore.model.enums.Type;
import ru.arrowin.socksstore.services.FilesService;
import ru.arrowin.socksstore.services.SocksOrderService;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;


@Service
public class SocksOrderServiceImpl implements SocksOrderService {

    private List<SocksOrder> orders = new LinkedList<>();

    private final FilesService filesService;

    public SocksOrderServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    public void init() {
        uploadOrdersFromFile();
    }

    @Override
    public void addOrder(SocksConsignment consignment, Type type) throws SaveFileException {
        SocksOrder order = new SocksOrder(type, consignment);
        orders.add(order);
        saveToFile();
    }


    @Override
    public void uploadOrdersFromFile() {
        try {
            String json = filesService.readOrdersFromFile();
            if (!json.isBlank()) {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                orders = mapper.readValue(json, new TypeReference<LinkedList<SocksOrder>>() {
                });

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ReadFileException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() throws SaveFileException {
        try {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(orders);
            filesService.saveOrdersToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new SaveFileException(e.getMessage());
        }
    }
}
