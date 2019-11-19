package com.hmlr123.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        // 天气更新时间
        @SerializedName("loc")
        public String updateTime;
    }
}

