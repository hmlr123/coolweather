package com.hmlr123.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 天气数据实体
 */
public class Weather {

    // 接口状态
    public String status;

    // 基础信息
    public Basic basic;

    public AQI aqi;

    // 实况天气
    public Now now;

    public Suggestion suggestion;

    // 天气预报
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
