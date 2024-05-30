package com.wposs.appfinanciera.Utils;

public enum DateFormatType {
    FORMAT_1("dd/MM/yyyy HH:mm:ss"),
    FORMAT_2("dd 'de' MMMM 'de' yyyy 'a las' hh:mm a");

    private String format;

    DateFormatType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
