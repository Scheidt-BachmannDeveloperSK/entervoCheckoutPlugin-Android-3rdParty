package com.scheidtbachmann.entervocheckoutplugin.delegation;


public enum LogLevel {

    OFF( 0),
    ERROR( 1),
    WARNING( 2),
    INFO( 3),
    TRACE( 4);

    public final int level;
    private LogLevel( int level) {
        this.level = level;
    }
}