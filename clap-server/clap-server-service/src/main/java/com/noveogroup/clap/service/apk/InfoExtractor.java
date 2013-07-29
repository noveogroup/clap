package com.noveogroup.clap.service.apk;

import com.noveogroup.clap.model.revision.ApkAndroidManifest;
import com.noveogroup.clap.model.revision.ApkStructure;

import java.util.zip.ZipEntry;

/**
 * @author Andrey Sokolov
 */
public interface InfoExtractor {

    /**
     * define if processing entry is interested and should be read in memory
     *
     * @param entry current entry
     * @param structure apk structure
     * @param androidManifest android manifest
     * @return true if entry enterested
     */
    boolean isEntryInterested(ZipEntry entry,
                              ApkStructure structure,
                              ApkAndroidManifest androidManifest);

    void processEntry(byte[] entryContent,
                      ZipEntry apkEntry,
                      ApkStructure structure,
                      ApkAndroidManifest androidManifest);
}
