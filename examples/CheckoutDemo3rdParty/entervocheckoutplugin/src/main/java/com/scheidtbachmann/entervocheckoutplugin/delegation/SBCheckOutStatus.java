package com.scheidtbachmann.entervocheckoutplugin.delegation;

public enum SBCheckOutStatus {

    INVALID,
    UNINITIALIZED,
    IDLE ,
    FLOW_STARTED,
    CLASSIFICATION_STARTED,
    CLASSIFICATION_FINISHED,
    PRICE_DISPLAY_STARTED,
    PRICE_DISPLAY_CANCELLED,
    PREPARATION_STARTED,
    PREPARATION_FINISHED,
    PAYMENT_STARTED,
    PAYMENT_CANCELLED,
    PAYMENT_FINISHED,
    STATUS_DISPLAY_STARTED,
    STATUS_DISPLAY_FINISHED,
    FLOW_FINISHED
}
