package com.example.raghav.grabahouse;

/**
 * Created by Raghav on 10/31/15.
 */
public class ServerUrl {
    //public static String url = "10.0.2.2";
    public static String url = "192.168.1.93";
    static String storetimeinterval_url = "http://"+url+":8080/GrabHouseServer/GetVisitTimeSlot";
    static String getHomeDetails_url = "http://"+url+":8080/GrabHouseServer/GetHomesJSON";
    static String getSingleHomeDetails_url = "http://"+url+":8080/GrabHouseServer/GetHomeDetails";
}
