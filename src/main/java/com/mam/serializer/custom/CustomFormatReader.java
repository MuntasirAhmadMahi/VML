package com.mam.serializer.custom;

public class CustomFormatReader {
    private final String buffer;
    int i = 0;

    public CustomFormatReader(String data) {
        buffer = data;
    }

    public int readInt() {
        int result = 0, sign = +1;

        if (buffer.charAt(i) == '-') {
            sign = -1;
            i++;
        }

        while (Character.isDigit(buffer.charAt(i))) {
            result *= 10;
            result += (buffer.charAt(i) - '0');
            i++;
        }
        i++; // $

        return result * sign;
    }

    public String readString(int len) {
        if (len == -1) {
            return null;
        }
        int j = i;
        i += len;

        return buffer.substring(j, i);
    }
}
