package com.noveogroup.clap.service.apk;

import java.io.File;

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
