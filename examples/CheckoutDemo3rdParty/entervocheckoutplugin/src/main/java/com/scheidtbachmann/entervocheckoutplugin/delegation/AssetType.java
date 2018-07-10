package com.scheidtbachmann.entervocheckoutplugin.delegation;

public enum AssetType {

    STYLESHEET( "styles.css"),
    IMAGE_SUCCESS( "happy_smiley.png"),
    IMAGE_FAIL( "sad_smiley.png"),
    IMAGE_BACKGROUND( "backgroiund.png");

    public String rawValue;

    AssetType( String rawValue) {
        this.rawValue = rawValue;
    }
}
