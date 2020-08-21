package com.example.pruebanavdrawer.Classes;

public class GetUrl {
    /*private final static String API_SERVER = "http://192.168.100.5:3333";*/
    /*private final static String WS_SERVER = "ws://192.168.100.5:3333/adonis-ws";*/
    private final static String API_SERVER = "http://165.227.23.126:8888";
    private final static String WS_SERVER = "ws://165.227.23.126:8888/adonis-ws";

    private static String TOKEN = "NOT SET";

    private static String USERNAME = "android@android.com";

    public static String getApiServer() {
        return API_SERVER;
    }

    public static void setToken(String token){
        TOKEN = "Bearer " + token;
    }

    public static String getToken() {
        return TOKEN;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static void setUsername(String username) {
        GetUrl.USERNAME = username;
    }

    public static String getWsServer(){
        return WS_SERVER;
    }

}
