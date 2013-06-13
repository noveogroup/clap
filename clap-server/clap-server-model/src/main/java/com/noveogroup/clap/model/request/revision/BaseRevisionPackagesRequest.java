package com.noveogroup.clap.model.request.revision;


/**
 * @author Andrey Sokolov
 */
public class BaseRevisionPackagesRequest {
    private StreamedPackage mainPackage;
    private StreamedPackage specialPackage;

    public StreamedPackage getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(final StreamedPackage mainPackage) {
        this.mainPackage = mainPackage;
    }

    public StreamedPackage getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(final StreamedPackage specialPackage) {
        this.specialPackage = specialPackage;
    }
}
