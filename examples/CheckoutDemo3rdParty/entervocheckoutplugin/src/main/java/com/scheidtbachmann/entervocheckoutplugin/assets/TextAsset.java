package com.scheidtbachmann.entervocheckoutplugin.assets;

import com.scheidtbachmann.entervocheckoutplugin.delegation.AssetType;

public class TextAsset {

    private String contents;
    private AssetType assetType;

    public TextAsset( String contents, AssetType assetType) {
        this.contents = contents;
        this.assetType = assetType;
    }

    public String getContents() { return contents;}
    public AssetType getAssetType() { return assetType;}
}
