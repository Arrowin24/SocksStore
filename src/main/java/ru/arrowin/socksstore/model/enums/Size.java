package ru.arrowin.socksstore.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Size {
    SIZE_26(26),
    SIZE_27(27),
    SIZE_28(28),
    SIZE_29(29),
    SIZE_30(30),
    SIZE_31(31),
    SIZE_32(32),
    SIZE_33(33);

    private final int size;

    Size(int size) {
        this.size = size;
    }

    @JsonValue
    public int getSize() {
        return size;
    }

    public static Size converter(int size) {
        for (Size sz : Size.values()) {
            if (sz.getSize() == size) {
                return sz;
            }
        }
        throw new IllegalArgumentException("Your size is not correct! Check it!");
    }
}
