package com.scheidtbachmann.entervocheckoutplugin.localization;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Text {

    private static Text instance = new Text();
    private final String DEFAULT_LANGUAGE = "en";
    private String lang = DEFAULT_LANGUAGE;
    private HashMap<String, String> texts;

    public static final String CHECKING_DATA              =  "CHECKING_DATA";
    public static final String PREPARING_PAYMENT          =  "PREPARING_PAYMENT";
    public static final String SENDING_PAYMENT            =  "SENDING_PAYMENT";
    public static final String PAY_NOW                    =  "PAY_NOW";
    public static final String CANCEL                     =  "CANCEL";
    public static final String OK                         =  "OK";
    public static final String THANK_YOU                  =  "THANK_YOU";
    public static final String OOPS                       =  "OOPS";
    public static final String NOTHING_TO_PAY             =  "NOTHING_TO_PAY";
    public static final String PAYMENT_RECEIVED           =  "PAYMENT_RECEIVED";
    public static final String LABEL_PARKING_DURATION     =  "LABEL_PARKING_DURATION";
    public static final String LABEL_AMOUNT_DUE           =  "LABEL_AMOUNT_DUE";
    public static final String FMT_DAYS                   =  "FMT_DAYS";
    public static final String FMT_HOURS                  =  "FMT_HOURS";
    public static final String LOADING_CONFIGURATION      =  "LOADING_CONFIGURATION";
    public static final String REMOVING_VAULT             =  "REMOVING_VAULT";
    public static final String NO_NETWORK                 =  "NO_NETWORK";
    public static final String TRY_AGAIN_LATER            =  "TRY_AGAIN_LATER";

    public static String get( String keyName) {
        if ( instance == null) {
            instance = new Text();
        }
        return instance.TXT( keyName);
    }

    public static Text getInstance() { return instance;}

    private Text() {

        String deviceLanguage = Locale.getDefault().getLanguage().toString();
        if ( localizationAvailableForLanguage( deviceLanguage)) {
            lang = deviceLanguage;
        }
        texts = loadTextsForLanguage( lang);
    }

    private String TXT( String keyName) {
        String value = texts.get( keyName);
        return value != null ? value : "#" + keyName + "#";
    }

    private HashMap<String, String> loadTextsForLanguage( String language) {

        HashMap<String, String> textList = new HashMap<String, String>();
        InputStream textFile = Text.class.getResourceAsStream("/assets/" + language + "_texts.json");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if ( textFile != null) {

            int character;
            try {
                character = textFile.read();
                while (character != -1)
                {
                    out.write( character);
                    character = textFile.read();
                }
                textFile.close();
            } catch (IOException e) {
            }
            String contents = out.toString();
            JsonObject contentsAsJson = new GsonBuilder().create().fromJson( contents, JsonObject.class);
            for (Map.Entry<String,JsonElement> entry:contentsAsJson.entrySet()) {
                textList.put( entry.getKey(), entry.getValue().getAsString());
            }
        }
        return textList;
    }

    private boolean localizationAvailableForLanguage( String language) {
        InputStream localizationFile = null;
        try {
            localizationFile = Text.class.getResourceAsStream("/assets/" + language + "_texts.json");
        } catch ( Exception ex) {
        }
        return localizationFile != null;
    }

    public boolean setLanguage( String language) {

        boolean available = true;
        if ( localizationAvailableForLanguage( language)) {
            lang = language;
        } else {
            lang = DEFAULT_LANGUAGE;
            available = false;
        }
        texts = loadTextsForLanguage( lang);
        return available;
    }

}
