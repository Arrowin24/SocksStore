package ru.arrowin.socksstore.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class SocksConsignment {
    private Socks socks;
    @Min(1) private int quantity;


    @JsonCreator
    public SocksConsignment(
            @JsonProperty("socks") Socks socks,
            @JsonProperty("quantity") int quantity)
    {
        this.socks = socks;
        setQuantity(quantity);
    }



    private void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
