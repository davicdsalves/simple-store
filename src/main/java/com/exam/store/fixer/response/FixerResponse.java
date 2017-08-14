package com.exam.store.fixer.response;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davi on 5/28/17.
 */
public class FixerResponse {
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

    public FixerResponse() {
        this.rates = new HashMap<>();
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
