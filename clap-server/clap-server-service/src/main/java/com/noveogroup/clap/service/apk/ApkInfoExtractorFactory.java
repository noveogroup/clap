package com.noveogroup.clap.service.apk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author Andrey Sokolov
 */
//TODO refactor extractors to reuse structure
public class ApkInfoExtractorFactory {

    private final File apkFile;

    public ApkInfoExtractorFactory(final File apkFile){
        this.apkFile = apkFile;
    }

    public IconExtractor createIconExtractor() {
        return new IconExtractor(apkFile);
    }

    public ManifestInfoExtractor createManifestInfoExtractor(){
        //TODO
        return null;
    }

    public StructureExtractor createStructureExtractor() {
        return new StructureExtractor(apkFile);
    }

}
