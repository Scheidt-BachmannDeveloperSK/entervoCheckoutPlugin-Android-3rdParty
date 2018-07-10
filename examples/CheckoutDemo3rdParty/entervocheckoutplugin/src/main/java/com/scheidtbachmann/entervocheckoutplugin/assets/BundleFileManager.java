package com.scheidtbachmann.entervocheckoutplugin.assets;

import android.app.Activity;
import android.content.Context;

import com.scheidtbachmann.entervocheckoutplugin.BuildConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class BundleFileManager {

    private final String FILEXT_CONFIG = ".cfg";
    private final String FILEXT_CUSTID = ".cust";
    private static final int BUFFERSIZE = 1024;
    private Context context;

    private final String bundleIdentifier = BuildConfig.PLUGIN_BUNDLE_ID;
    private final String frameworkName = BuildConfig.FRAMEWORK_NAME;

    public BundleFileManager( Context context) {
        super();
        this.context = context;
    }

    public String documentRootUrl() {

        String dir = appDocUrl() + File.separator+ frameworkName;
        return dir;
    }

    public String appDocUrl() {

        String path = context.getFilesDir().getPath();
        return path;
    }

    public boolean createDocRootIfNotExists() {

        boolean baseDirWasPresent = false;
        File baseDir = new File ( documentRootUrl());
        if ( !baseDir.exists()) {
            baseDir.mkdir();
        } else {
            baseDirWasPresent = true;
        }

        if ( !baseDirWasPresent && baseDir.exists()) {

            return populateFromBundle();
        }
        return false;
    }

    private boolean populateFromBundle() {

        String[] fileTypes = {"html", "css", "png", "jpg"};
        String[]assets = getAllAssets();
        for ( String asset : assets) {

            if ( matches( asset, fileTypes)) {
                if ( !copyFromBundleToDocumentRoot( asset)) {
                    return false;
                }
            }
        }
        return true;
    }

    private String[] getAllAssets() {

        String[] assets = null;
        try {
            assets = context.getAssets().list("");
        } catch ( IOException ioEx) {
                ioEx.printStackTrace();
        }
        return assets;
    }

    private boolean copyFromBundleToDocumentRoot( String assetName) {

        boolean copyOk = true;

        try {
            InputStream source = context.getAssets().open(assetName);
            String outputPath = documentRootUrl() + File.separator + assetName;
            FileOutputStream target = new FileOutputStream(outputPath);
            int size;
            byte[] buffer = new byte[BUFFERSIZE];

            try {
                while ((size = source.read(buffer, 0, buffer.length)) != -1) {
                    target.write(buffer, 0, size);
                }
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
                copyOk = false;
            }
        } catch ( IOException ioEx) {
            ioEx.printStackTrace();
            copyOk = false;
        }
        return copyOk;
    }

    private boolean matches( String fileName, String[] extensions) {

        for ( String ext: extensions) {
            if ( matches( fileName, ext)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches( String fileName, String ext) {

        return fileName.endsWith(ext);
    }

    public String contentsOfFile( String fileName) {

        final String UTF8_CHARSET = "UTF-8";

        String contents = "";
        FileInputStream is = null;
        byte[] bytes = null;

        try {
            File inputFile = new File(documentRootUrl() + File.separator + fileName);
            bytes = new byte[(int)inputFile.length()];
            is = new FileInputStream(inputFile);
            is.read( bytes);
            contents = new String( bytes, UTF8_CHARSET);

        } catch (IOException ioEx) {

        } finally {
            if ( is != null) {
                try {
                    is.close();
                } catch ( IOException ioEx) {
                }
            }
        }
        return contents;
    }

    public static boolean writeStringToLocalFile(Activity context, String fileName, String data) {

        boolean ok = true;
        try {
            FileOutputStream file = context.openFileOutput( fileName, Activity.MODE_WORLD_READABLE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( file, Charset.forName("UTF-8")),BUFFERSIZE);
            bw.write( data);
            bw.close();
            file.close();

        } catch ( Exception ex) {  ok = false; }

        return ok;
    }


    public static String readStringFromLocalFile(Activity context, String fileName) {

        String data = "";
        try {
            int character;
            FileInputStream file = context.openFileInput( fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(file, Charset.forName("UTF-8")),BUFFERSIZE);
            data = br.readLine();
            br.close();
            file.close();

        } catch ( Exception ex) { ex.printStackTrace();}

        return data;
    }

    public String getFrameworkName() {
        return frameworkName;
    }
}
