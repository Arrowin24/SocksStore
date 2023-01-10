package ru.arrowin.socksstore.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public Socks(
            @JsonProperty("color") String color,
            @JsonProperty("size") int size,
            @JsonProperty("cottonPart") int cottonPart)
    {
        this.color = Color.valueOf(color.toUpperCase());
        this.size = sizeConverter(size);
        setCottonPart(cottonPart);
    }

    public enum Color {
        BLACK, WHITE, RED, ORANGE, YELLOW, GREEN, BLUE
    }

    public enum Size {
        SIZE_26(26), SIZE_27(27), SIZE_28(28), SIZE_29(29), SIZE_30(30), SIZE_31(31), SIZE_32(32), SIZE_33(33);

        private final int size;


        Size(int size) {
            this.size = size;
        }

        @JsonValue
        public int getSize() {
            return size;
        }
    }

    private Size sizeConverter(int size) {
        return switch (size) {
            case 26 -> Size.SIZE_26;
            case 27 -> Size.SIZE_27;
            case 28 -> Size.SIZE_28;
            case 29 -> Size.SIZE_29;
            case 30 -> Size.SIZE_30;
            case 31 -> Size.SIZE_31;
            case 32 -> Size.SIZE_32;
            case 33 -> Size.SIZE_33;
            default -> throw new IllegalArgumentException();
        };
    }


    private void setCottonPart(int cottonPart) {
        if (cottonPart >= 0 && cottonPart <= 100) {
            this.cottonPart = cottonPart;
        } else {
            throw new IllegalArgumentException();
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
