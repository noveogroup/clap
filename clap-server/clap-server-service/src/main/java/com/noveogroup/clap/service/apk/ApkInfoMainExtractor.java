package com.noveogroup.clap.service.apk;

import com.noveogroup.clap.model.revision.ApkAndroidManifest;
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
public class ApkInfoMainExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApkInfoMainExtractor.class);

    private static final char ZIP_ARCHIVE_DELIMITER = '/';

    private final File apkFile;

    private final Map<String, ApkEntry> directoriesMap = new HashMap<String, ApkEntry>();

    private final Map<InfoExtractor, List<ApkEntry>> infoExtractorsMap = new HashMap<InfoExtractor, List<ApkEntry>>();

    private final ManifestInfoExtractor manifestInfoExtractor = new ManifestInfoExtractor();

    private ApkStructure apkStructure;

    public ApkInfoMainExtractor(final File apkFile) {
        this.apkFile = apkFile;
    }

    public void addInfoExtractor(final InfoExtractor infoExtractor) {
        infoExtractorsMap.put(infoExtractor, new ArrayList<ApkEntry>());
    }

    public ApkAndroidManifest getFoundManifest() {
        return manifestInfoExtractor.getFoundManifest();
    }

    public ApkStructure getStructure() {
        return apkStructure;
    }

    public void processApk() throws IOException {
        apkStructure = readStructure();
        final ApkAndroidManifest foundManifest = manifestInfoExtractor.getFoundManifest();
        final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(apkFile));
        ZipEntry zipentry = zipInputStream.getNextEntry();
        while (zipentry != null) {
            byte[] entryContent = null;
            for (final InfoExtractor infoExtractor : infoExtractorsMap.keySet()) {
                if (infoExtractor.isEntryInterested(zipentry, apkStructure, foundManifest)) {
                    if (entryContent == null) {
                        final long zipentrySize = zipentry.getSize();
                        if (zipentrySize > Integer.MAX_VALUE) {
                            throw new IllegalStateException(
                                    "size of that entry(" + zipentry + ") is too damn high: " + zipentrySize);
                        } else {
                            entryContent = new byte[(int) zipentrySize];
                            zipInputStream.read(entryContent, 0, (int) zipentrySize);
                        }
                    }
                    infoExtractor.processEntry(entryContent, zipentry, apkStructure, foundManifest);
                }
            }
            zipentry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    private ApkStructure readStructure() throws IOException {
        directoriesMap.clear();
        final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(apkFile));
        ZipEntry zipentry = zipInputStream.getNextEntry();
        final ApkStructure ret = new ApkStructure();
        final ApkEntry rootEntry = new ApkEntry();
        ret.setRootEntry(rootEntry);
        rootEntry.setEntryName("root");
        rootEntry.setDirectory(true);
        rootEntry.setInnerEntries(new ArrayList<ApkEntry>());
        while (zipentry != null) {
            processEntry(rootEntry, zipentry);
            manifestInfoExtractor.getManifest(zipentry,zipInputStream);
            zipentry = zipInputStream.getNextEntry();
        }
        LOGGER.debug("apk structure : " + ret);
        zipInputStream.close();
        return ret;
    }

    private void processEntry(final ApkEntry rootEntry, final ZipEntry zipEntry) {
        final String entryName = zipEntry.getName();
        final String[] path = StringUtils.split(entryName, ZIP_ARCHIVE_DELIMITER);
        if (path != null && path.length > 0) {
            addEntry(rootEntry, entryName, path);
        } else {
            throw new IllegalStateException("shoudn't be null or empty");
        }
    }

    private void addEntry(final ApkEntry rootEntry, final String entryName, final String[] path) {
        for (int i = 0; i < path.length; i++) {
            final String currentPathEntryName = StringUtils.join(path, ZIP_ARCHIVE_DELIMITER, 0, i + 1);
            ApkEntry currentPathEntry = directoriesMap.get(currentPathEntryName);
            if (currentPathEntry == null) {
                currentPathEntry = new ApkEntry();
                currentPathEntry.setEntryName(currentPathEntryName);
                if (i < path.length - 1) {
                    currentPathEntry.setDirectory(true);
                    directoriesMap.put(currentPathEntryName, currentPathEntry);
                }
                final ApkEntry currentPathEntryParent;
                if (i > 0) {
                    currentPathEntryParent = directoriesMap.get(StringUtils.join(path, ZIP_ARCHIVE_DELIMITER, 0, i));
                } else {
                    currentPathEntryParent = rootEntry;
                }
                List<ApkEntry> currentPathEntryParentInnerEntries = currentPathEntryParent.getInnerEntries();
                if (currentPathEntryParentInnerEntries == null) {
                    currentPathEntryParentInnerEntries = new ArrayList<ApkEntry>();
                    currentPathEntryParent.setInnerEntries(currentPathEntryParentInnerEntries);
                }
                currentPathEntryParentInnerEntries.add(currentPathEntry);
            }
        }

    }
}
