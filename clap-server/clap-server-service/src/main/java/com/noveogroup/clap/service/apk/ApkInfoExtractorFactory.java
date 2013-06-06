package com.noveogroup.clap.service.apk;

/**
 * @author Andrey Sokolov
 */
public class ApkInfoExtractorFactory {

    public static final ApkInfoExtractorFactory getInstance(byte[] apkFile){
        return null;
    }

    private ApkInfoExtractorFactory(){
        //TODO
    }

    public IconExtractor createIconExtractor(){
        //TODO
        return null;
    }

    public ManifestInfoExtractor createManifestInfoExtractor(){
        //TODO
        return null;
    }
}
