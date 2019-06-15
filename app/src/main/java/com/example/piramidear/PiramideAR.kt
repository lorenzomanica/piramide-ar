package com.example.piramidear

import android.app.Application
import org.artoolkitx.arx.arxj.assets.AssetHelper


class PiramideAR : Application() {


    override fun onCreate() {
        super.onCreate()
        initializeInstance()
    }

    protected fun initializeInstance() {

        // Unpack assets to cache directory so native library can read them.
        // N.B.: If contents of assets folder changes, be sure to increment the
        // versionCode integer in the modules build.gradle file.
        val assetHelper = AssetHelper(assets)
        assetHelper.cacheAssetFolder(this, "patterns")
        assetHelper.cacheAssetFolder(this, "cparam_cache")
    }
}
