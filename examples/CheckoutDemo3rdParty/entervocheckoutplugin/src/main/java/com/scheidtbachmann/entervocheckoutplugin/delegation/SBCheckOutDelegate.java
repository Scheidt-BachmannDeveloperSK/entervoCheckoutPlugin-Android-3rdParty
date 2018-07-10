package com.scheidtbachmann.entervocheckoutplugin.delegation;

import com.scheidtbachmann.entervocheckoutplugin.dto.SBCheckOutTransaction;

public interface SBCheckOutDelegate {

    public void onMessage( LogLevel level, String message);
    public void onError( String message);
    public void onStatus( SBCheckOutStatus newStatus, SBCheckOutTransaction info);
    public void onConductPayment( String sessionToken);
}
