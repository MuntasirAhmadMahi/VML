package com.mam.serializer.custom;

public class CustomFormatWriter {
    private final StringBuilder buffer = new StringBuilder();

    public void write(String s) {
        if (s == null) {
            buffer.append("-1$");
        } else {
            buffer.append(s.length()).append("$").append(s);
        }
    }

    public void write(int n) {
        buffer.append(n).append("$");
    }

    public String result() {
        return buffer.toString();
    }
}
