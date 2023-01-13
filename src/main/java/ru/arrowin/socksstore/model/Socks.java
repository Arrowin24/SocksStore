package ru.arrowin.socksstore.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.arrowin.socksstore.model.enums.Color;
import ru.arrowin.socksstore.model.enums.Size;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socks {

    private Color color;
    private Size size;
    @Min(1)
    @Max(100)
    private int cottonPart;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Socks(@JsonProperty("color") String color,@JsonProperty("size") int size,@JsonProperty("cottonPart") int cottonPart)
    {
        this.color = Color.valueOf(color.toUpperCase());
        this.size = Size.converter(size);
        setCottonPart(cottonPart);
    }

     private void setCottonPart(int cottonPart) {
        if (cottonPart >= 0 && cottonPart <= 100) {
            this.cottonPart = cottonPart;
        } else {
            throw new IllegalArgumentException("Cotton part is not correct. Check it!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socks socks = (Socks) o;
        return cottonPart == socks.cottonPart && color == socks.color && size == socks.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, cottonPart);
    }
}
