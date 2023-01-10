package ru.arrowin.socksstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocksOrder {

    private TypeOrder typeOrder;
    private SocksConsignment consignment;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate date;
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime time;

    public enum TypeOrder {
        ADD("Приемка"), SELL("Выдача"), DELETE("Списание");
        private final String description;

        TypeOrder(String description) {
            this.description = description;
        }
        @JsonValue
        public String getDescription() {
            return description;
        }
    }

}
