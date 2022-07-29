package com.example.logAnalyser.dto;

public enum LogState {

    STARTED("STARTED"),
    FINISHED("FINISHED");

    private final String value;

    LogState(String value) {
        this.value = value;
    }

}
