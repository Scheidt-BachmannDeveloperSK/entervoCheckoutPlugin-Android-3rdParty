package com.scheidtbachmann.entervocheckoutplugin.dto;


import java.util.HashMap;

public class PluginConfig {

    private String apiKey;
    private int appId;
    private String baseUrl;
    private String operatorName;
    private String subtitle;
    private String mandatorId;
    private String expiration;
    private String pspProvider;

    public PluginConfig() {
    }

    public HashMap<String,String> asDictionary() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put( "apiKey", apiKey);
        map.put( "appId", String.valueOf( appId));
        map.put( "baseUrl", baseUrl);
        map.put( "operatorName", operatorName);
        map.put( "subtitle", subtitle);
        map.put( "mandatorId", mandatorId);
        map.put( "expiration", expiration);
        map.put( "pspProvider", pspProvider);
        return map;
    }

    public String getApiKey() { return apiKey;}
    public int getAppId() {return appId;}
    public String getBaseUrl() { return baseUrl;}
    public String getOperatorName() {return operatorName;}
    public String getSubtitle() { return subtitle;}
    public String getMandatorId() { return mandatorId;}
    public String getExpiration() { return expiration;}
    public String getPspProvider() { return pspProvider;}

    public void setApiKey( String apiKey) { this.apiKey = apiKey;}
    public void setAppId( int appId) { this.appId = appId;}
    public void setBaseUrl( String baseUrl) { this.baseUrl = baseUrl;}
    public void setOperatorName( String operatorName) { this.operatorName = operatorName;}
    public void setSubtitle( String subtitle) {this.subtitle = subtitle;}
    public void setMandatorId( String mandatorId) {this.mandatorId = mandatorId;}
    public void setExpiration( String expiration) { this.expiration = expiration;}
    public void setPspProvider( String pspProvider) {this.pspProvider = pspProvider;}
}