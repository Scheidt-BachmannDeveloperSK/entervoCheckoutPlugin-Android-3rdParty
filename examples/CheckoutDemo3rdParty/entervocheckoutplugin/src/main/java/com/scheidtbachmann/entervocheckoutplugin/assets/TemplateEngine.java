package com.scheidtbachmann.entervocheckoutplugin.assets;

import android.content.Context;

import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;

import java.io.File;
import java.util.HashMap;

public class TemplateEngine {

    private Context context;
    private BundleFileManager manager;
    private HashMap<String,String> data;
    private String resourceName;
    private final String RESOURCE_EXT = ".html";

    public TemplateEngine( String resourceName, HashMap<String,String> data) {
        context = Plugin.getInstance().getContext();
        manager = new BundleFileManager( context);
        this.data = data;
        this.resourceName = resourceName;
    }

    public TemplateEngine( String resourceName, String[] data) {

        HashMap<String,String> hmData = new HashMap<String,String>();
        int elements = data.length / 2;
        for ( int i=0; i < elements; i++) {
            hmData.put( data[i*2], data[i*2+1]);
        }
        context = Plugin.getInstance().getContext();
        manager = new BundleFileManager( context);
        this.data = hmData;
        this.resourceName = resourceName;
    }

    public String merge() {

        String contents = manager.contentsOfFile( resourceName + RESOURCE_EXT);
        return merge( contents, data);
    }

    public String merge( String originalContents, String key, String value) {

        String contents = new String( originalContents);
        String token = "###" + key + "###";
        contents = contents.replaceAll( token, value);
        return contents;
    }

    public String merge( String originalContents, HashMap<String,String> data) {

        String contents = new String( originalContents);
        for ( String key: data.keySet()) {
            String token = "###" + key + "###";
            String value = data.get( key);
            contents = contents.replaceAll( token, value);
        }
        return contents;
    }
}
