package ru.arrowin.socksstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocksConsignment {
    private Socks socks;
    @Min(1) private int quantity;


    private void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Вы не можете работать с нулевым или отрицательным числом носков");
        }
    }
}
