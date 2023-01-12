package ru.arrowin.socksstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.arrowin.socksstore.model.enums.Type;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocksOrder {

    private Type type;
    private SocksConsignment consignment;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate date;
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime time;

    public SocksOrder(Type type, SocksConsignment consignment) {
        this.type = type;
        this.consignment = consignment;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

}
