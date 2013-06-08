package com.noveogroup.clap.service.apk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.zip.ZipInputStream;

/**
 * @author Andrey Sokolov
 */
public class ApkInfoExtractorFactory {

    public static final ApkInfoExtractorFactory getInstance(byte[] apkFile) throws FileNotFoundException {
        //ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(apkFile));
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(apkFile));
        return new ApkInfoExtractorFactory(zipInputStream);
    }

    private final ZipInputStream zipInputStream;

    private ApkInfoExtractorFactory(ZipInputStream zipInputStream){
        this.zipInputStream = zipInputStream;
    }

    public IconExtractor createIconExtractor(){
        return new IconExtractor(zipInputStream);
    }

    public ManifestInfoExtractor createManifestInfoExtractor(){
        //TODO
        return null;
    }
}
