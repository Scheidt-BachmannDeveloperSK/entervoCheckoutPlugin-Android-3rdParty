package com.scheidtbachmann.entervocheckoutplugin.assets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.delegation.AssetType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class AssetManager {

    private static AssetManager instance = new AssetManager();
    private ArrayList<DrawableAsset> customDrawables = new ArrayList<DrawableAsset>();
    private ArrayList<TextAsset> customTextAssets = new ArrayList<TextAsset>();

    private AssetManager() {

    }

    /*
     * setAsset(Drawable,AssetType)
     * receives a Drawable to be used for one of: IMAGE_SUCCESS, IMAGE_FAIL, IMAGE_BACKGROUND
     */

    public static void setAsset( Drawable asset, AssetType assetType) {

        if (    assetType == AssetType.IMAGE_SUCCESS ||
                assetType == AssetType.IMAGE_FAIL ||
                assetType == AssetType.IMAGE_BACKGROUND) {

            if (Plugin.getInstance().getContext() != null) {
                AssetManager.instance.setDrawableAsset(asset, assetType);
            } else {
                instance.customDrawables.add(new DrawableAsset(asset, assetType));
            }
        }
    }

    public static void setAsset( String contents, AssetType assetType) {

        if ( assetType == AssetType.STYLESHEET) {
            if ( Plugin.getInstance().getContext() != null) {
                AssetManager.instance.setTextAsset( contents, assetType);

            } else {
                instance.customTextAssets.add( new TextAsset( contents, assetType));
            }
        }
    }

    public static void applyCachedAssets() {
        for ( DrawableAsset a : instance.customDrawables) {
            instance.setDrawableAsset( a.getAsset(), a.getAssetType());
            instance.customDrawables.remove( a);
        }
        for ( TextAsset a : instance.customTextAssets) {
            instance.setTextAsset( a.getContents(), a.getAssetType());
            instance.customTextAssets.remove( a);
        }
    }

    private void setTextAsset( String contents, AssetType assetType) {

        File targetFile = new File( Plugin.getInstance().getFileManager().documentRootUrl() + File.separator + assetType.rawValue);
        try {
            FileOutputStream targetStream = new FileOutputStream(targetFile);
            targetStream.write( contents.getBytes(), 0, contents.getBytes().length);
            targetStream.flush();
            targetStream.close();

        } catch ( FileNotFoundException fnnEx) {

        } catch ( IOException ioEx) {

        }
    }

    private void setDrawableAsset( Drawable asset, AssetType assetType) {
        final int FULL_QUALITY = 100;

            Bitmap bitmap = ((BitmapDrawable) asset).getBitmap();
            File targetFile = new File( Plugin.getInstance().getFileManager().documentRootUrl() + File.separator + assetType.rawValue);
            try {
                FileOutputStream targetStream = new FileOutputStream(targetFile);
                bitmap.compress( Bitmap.CompressFormat.PNG, FULL_QUALITY, targetStream);
                targetStream.flush();
                targetStream.close();

            } catch ( FileNotFoundException fnnEx) {

            } catch ( IOException ioEx) {
        }
    }
}
