package com.pv.mfl.bootstrap;


import io.dropwizard.assets.AssetsBundle;

public class StaticAssetsBundle extends AssetsBundle {
    public StaticAssetsBundle() {
        super("/assets", "/a", "", "mfl_assets");
    }
}