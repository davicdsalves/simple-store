package com.exam.store.model;

public enum Currency {
    AUD,
    BGN,
    BRL,
    CAD,
    CHF,
    CNY,
    CZK,
    DKK,
    GBP,
    HKD,
    HRK,
    HUF,
    IDR,
    ILS,
    INR,
    JPY,
    KRW,
    MXN,
    MYR,
    NOK,
    NZD,
    PHP,
    PLN,
    RON,
    RUB,
    SEK,
    SGD,
    THB,
    TRY,
    USD,
    ZAR,
    EUR,
    UNKNOWN;

    public static Currency get(String name) {
        try {
            return Currency.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return Currency.UNKNOWN;
        }
    }
}
