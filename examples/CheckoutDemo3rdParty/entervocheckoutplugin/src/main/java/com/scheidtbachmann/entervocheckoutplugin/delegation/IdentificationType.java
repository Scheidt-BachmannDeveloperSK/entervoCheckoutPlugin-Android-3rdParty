package com.scheidtbachmann.entervocheckoutplugin.delegation;

public enum IdentificationType {

    BARCODE( "BARCODE"),
    LICENSEPLATE( "LICENSEPLATE"),
    MOBILEID( "MOBILEID");

    public String rawValue;

    private IdentificationType( String value) {
        rawValue = value;
    }
}