package com.hmlr123.coolweather.db;

import org.litepal.exceptions.DataSupportException;

/**
 * 省份实体
 */
public class Province extends DataSupportException {
    public Province(String errorMessage) {
        super(errorMessage);
    }

    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
