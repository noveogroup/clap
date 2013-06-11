package com.noveogroup.clap.service.apk;

import com.noveogroup.clap.model.revision.ApkEntry;
import com.noveogroup.clap.model.revision.ApkStructure;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Andrey Sokolov
 */
public class StructureExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StructureExtractor.class);

    private static final char ZIP_ARCHIVE_DELIMITER = '/';

    private final File apkFile;

    private final Map<String,ApkEntry> directoriesMap = new HashMap<String, ApkEntry>();

    public StructureExtractor(final File apkFile) {
        this.apkFile = apkFile;
    }

    public ApkStructure getStructure() throws IOException {
        directoriesMap.clear();
        final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(apkFile));
        ZipEntry zipentry = zipInputStream.getNextEntry();
        final ApkStructure ret = new ApkStructure();
        final ApkEntry rootEntry = new ApkEntry();
        ret.setRootEntry(rootEntry);
        rootEntry.setEntryName("root");
        rootEntry.setInnerEntries(new ArrayList<ApkEntry>());
        while (zipentry != null) {
            processEntry(rootEntry, zipentry);
            zipentry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        return ret;
    }

    private void processEntry(final ApkEntry rootEntry, final ZipEntry zipEntry){
        final String entryName = zipEntry.getName();
        final ApkEntry apkEntry = new ApkEntry();
        final String[] path = StringUtils.split(entryName, ZIP_ARCHIVE_DELIMITER);
        if(path != null){
            ApkEntry toAdd = rootEntry;
            if(path.length > 1){
            }
            apkEntry.setEntryName(entryName);
        } else {
            throw new IllegalStateException("shoudn't be null");
        }
    }

    private ApkEntry findParent(final ApkEntry rootEntry, final String entryName, final String[] path){
        for (int i=0; i < path.length -1; i++){
            final String currentPathEntryName = StringUtils.join(path,ZIP_ARCHIVE_DELIMITER,0,i);
            ApkEntry currentPathEntry = directoriesMap.get(currentPathEntryName);
            if(currentPathEntry == null){
                currentPathEntry = new ApkEntry();
                ApkEntry currentPathEntryParent;
                if(i>0){
                    currentPathEntryParent = directoriesMap.get(StringUtils.join(path,ZIP_ARCHIVE_DELIMITER,0,i-1));
                } else {
                    currentPathEntryParent = rootEntry;
                }
                List<ApkEntry> currentPathEntryParentInnerEntries = currentPathEntryParent.getInnerEntries();
                if(currentPathEntryParentInnerEntries == null){
                    currentPathEntryParentInnerEntries = new ArrayList<ApkEntry>();
                    currentPathEntryParent.setInnerEntries(currentPathEntryParentInnerEntries);
                }
                currentPathEntryParentInnerEntries.add(currentPathEntry);
            }
        }
    }
}
