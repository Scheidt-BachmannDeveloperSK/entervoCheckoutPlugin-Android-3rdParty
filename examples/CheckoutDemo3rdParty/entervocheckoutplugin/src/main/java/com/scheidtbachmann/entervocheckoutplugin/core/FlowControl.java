package com.scheidtbachmann.entervocheckoutplugin.core;

import com.scheidtbachmann.entervocheckoutplugin.delegation.LogLevel;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutDelegate;

public class FlowControl {

    public static void raise( final String errorMessage) {

        final SBCheckOutDelegate delegate = Plugin.getInstance().getDelegate();
        if ( delegate != null) {
            Thread thread = new Thread() {
                public void run() {
                    delegate.onError( errorMessage);
                }
            };
            thread.start();
        }
    }

    public static void log( final LogLevel level, final String message) {

        final SBCheckOutDelegate delegate = Plugin.getInstance().getDelegate();
        if ( delegate != null) {
            Thread thread = new Thread() {
                public void run() {
                    delegate.onMessage( level, message);
                }
            };
            thread.start();
        }
    }
}
