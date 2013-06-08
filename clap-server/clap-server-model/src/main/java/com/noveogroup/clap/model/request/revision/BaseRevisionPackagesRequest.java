package com.noveogroup.clap.model.request.revision;

import java.io.InputStream;

/**
 * @author Andrey Sokolov
 */
public class BaseRevisionPackagesRequest {
    private StreamedPackage mainPackage;
    private StreamedPackage specialPackage;

    public StreamedPackage getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(StreamedPackage mainPackage) {
        this.mainPackage = mainPackage;
    }

    public StreamedPackage getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(StreamedPackage specialPackage) {
        this.specialPackage = specialPackage;
    }
}
