# 介绍

《第一行代码》大demo 酷欧天气练习，模仿实现，后面逐渐补充增强

# 实现功能

1. 省市县数据的展示选择
2. 显示全国各地的天气信息
3. 自由切换城市信息，查看其他城市信息
4. 提供手动和自动刷新天气信息



# 实现步骤

1. 添加依赖
2. 创建数据库和表
3. 遍历全国省市县数据
4. 显示天气信息
5. 手动更新天气和切换城市
6. 后台自动更新
7. 修改图标和名称



详细实现看书



# 涉及技术

1. 本地数据存储
2. 网络请求okhttp使用，回调使用
3. GSON实现JSON数据转换
4. 界面布局技巧
5. 图片加载渲染glide
6. 后台定时服务



## 本地数据存储

本地数据存储两种形式，Litepal数据库和SharedPreferences。

1. Litepal主要用于存储表数据，功能上主要是省份等数据的本地缓存，数据量不大。使用上涉及到表的创建（版本问题），实体映射，以及常见的增删改查。

   实体类继承`LitePalSupport`

   ```java
   public class Province extends LitePalSupport 
   ```

   调用继承的方法实现数据存储

   ```java
   county.save();
   ```

   详细使用参考官方文档https://github.com/LitePalFramework/LitePal，书中代码较老，使用最新的官方说明

   

2. SharedPreferences主要用于当前城市的天气缓存数据，方便序列化存储，减少网络请求。

   数据存储：

   ```java
       SharedPreferences.Editor editor = PreferenceManager
               .getDefaultSharedPreferences(WeatherActivity.this).edit();
       editor.putString("bing_pic", bingPic);
       editor.apply();
   ```

   数据获取：

   ```java
       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       String weatherString = prefs.getString("weather", null);
   ```



## okhttp

https://github.com/square/okhttp

```java
    /**
     * 发送网络请求
     *
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
```

注意callback回调函数，在调用该方法的时候实现回调函数



## GSON实现JSON数据转换

https://github.com/google/gson

```java
    JSONArray allCounties = new JSONArray(response);
    for (int i = 0; i < allCounties.length(); i++) {
        JSONObject countyObject = allCounties.getJSONObject(i);
        County county = new County();
        county.setCountyName(countyObject.getString("name"));
        county.setWeatherId(countyObject.getString("weather_id"));
        county.setCityId(cityId);
        county.save();
    }
```





## **界面布局技巧**

1. 先实现通用碎片，选择地区的布局使用线性布局，分成两部分，上部分包括返回图片，文字（显示当前地区），使用`RelativeLayout`。下部分使用`ListView`，用于渲染显示列表数据(也可以使用RecyclerView，但是还得单独写个数据适配ViewHolder，比较麻烦。在单行数据布局较少的情况下，使用ListView可能较好些)

   此部分除了在主界面会使用外，在滑动菜单中也会使用

2. 天气视图界面布局 weather，包括头部标题`title`，当前天气信息`now`，天气预报`forecast`，空气质量`aqi`以及建议`suggestion`及部分组成。比较巧妙的是后追加的背景图片、滚动条、下拉刷新、滑动菜单，详细布局如下

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:background="@color/colorPrimary">
   
       <!--背景图片-->
       <ImageView
           android:id="@+id/bing_pic_img"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           android:scaleType="centerCrop" />
   
   
       <!--滑动菜单-->
       <androidx.drawerlayout.widget.DrawerLayout
           android:id="@+id/drawer_layout"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
           <!--下拉刷新-->
           <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
               android:id="@+id/swipe_refresh"
               android:layout_width="match_parent"
               android:layout_height="match_parent">
               <!--滚动条-->
               <ScrollView
                   android:id="@+id/weather_layout"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent"
                   android:overScrollMode="never"
                   android:scrollbars="none">
   
   
                   <!--android:fitsSystemWindows="true"
                   针对透明的状态栏会自动添加一个值等于状态栏高度的paddingTop；
                   针对透明的系统导航栏会自动添加一个值等于导航栏高度的paddingBottom
                   https://www.cnblogs.com/xgjblog/p/9517645.html -->
                   <LinearLayout
                       android:layout_width="match_parent"
                       android:layout_height="wrap_content"
                       android:fitsSystemWindows="false"
                       android:orientation="vertical">
   
                       <!--标题-->
                       <include layout="@layout/title" />
   
                       <!--现在天气-->
                       <include layout="@layout/now" />
   
                       <!--预报天气-->
                       <include layout="@layout/forecast" />
   
                       <!--aqi-->
                       <include layout="@layout/aqi" />
   
                       <!--建议-->
                       <include layout="@layout/suggestion" />
                   </LinearLayout>
   
               </ScrollView>
   
           </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
   
           <!--滑动菜单显示内容-->
           <fragment
               android:id="@+id/choose_area_fragment"
               android:name="com.hmlr123.coolweather.ChooseAreaFragment"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               android:layout_gravity="start" />
   
       </androidx.drawerlayout.widget.DrawerLayout>
   
   
   </FrameLayout>
   ```

   

   ## 图片加载渲染glide

   ```java
   Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
   ```

   

   

   ## 后台定时服务

   创建服务继承Service

   在启动服务的时候，将定时任务启动

   ```java
       AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
       int anHour = 8 * 60 * 60 * 1000;
       long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
       Intent i = new Intent(this, AutoUpdateService.class);
       PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
       manager.cancel(pendingIntent);
       manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
   ```

   



# 遇到的Bug

1. 滑动菜单选择县的时候没有更新全局变量weatherId，导致调用下拉刷新的数据还是老的数据

2. 滑动菜单背景式透明的，映射到滑动菜单主要显示的内容，导致内容看不清，将滑动菜单隐藏的内容（选择县的碎片）背景颜色设置成白色

3. 透明状态栏，当前程序退出显示（活动被销毁），再显示的时候状态栏会出现白色。

   解决办法：不适用状态栏，将状态栏隐藏

   再WeatherActivity中

   ```java
           // 隐藏标题栏
           requestWindowFeature(Window.FEATURE_NO_TITLE);
           // 隐藏状态栏
           getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                   WindowManager.LayoutParams.FLAG_FULLSCREEN);
   ```

   



# 额外

在Gradle中使用依赖，可以通过浏览器查看SQLite内容，不用root权限

```groovy
// 数据库调试查看 非root也能使用 调用浏览器
    debugImplementation 'com.amitshekhar.android:debug-db:1.0.6'
```





