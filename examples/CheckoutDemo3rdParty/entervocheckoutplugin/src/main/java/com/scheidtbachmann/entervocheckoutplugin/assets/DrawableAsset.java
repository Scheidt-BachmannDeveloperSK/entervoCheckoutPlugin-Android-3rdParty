package com.scheidtbachmann.entervocheckoutplugin.assets;

import android.graphics.drawable.Drawable;

import com.scheidtbachmann.entervocheckoutplugin.delegation.AssetType;

public class DrawableAsset {

    private Drawable asset;
    private AssetType assetType;

    public DrawableAsset( Drawable asset, AssetType assetType) {
        this.asset = asset;
        this.assetType = assetType;
    }

    public Drawable getAsset() { return asset;}
    public AssetType getAssetType() { return assetType;}
}
