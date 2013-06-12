package com.noveogroup.clap.service.apk;

import com.noveogroup.clap.service.apk.exception.ExtractingInfoException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Andrey Sokolov
 */
public class IconExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(IconExtractor.class);

    private static final int ICONS_PATH_MAX_PRIORITY = 5;
    private final Map<Integer, IconPathTemplate> iconsPathPriorityMap;
    private final File apkFile;
    private String foundIconEntryName;
    private int foundIconEntrySize;
    private int foundIconPriority;

    IconExtractor(final File apkFile) {
        this.apkFile = apkFile;
        iconsPathPriorityMap = new HashMap<Integer, IconPathTemplate>();
        iconsPathPriorityMap.put(1, new IconPathTemplate("", 1));
        iconsPathPriorityMap.put(2, new IconPathTemplate("ldpi", 2));
        iconsPathPriorityMap.put(3, new IconPathTemplate("mdpi", 3));
        iconsPathPriorityMap.put(4, new IconPathTemplate("hdpi", 4));
        iconsPathPriorityMap.put(5, new IconPathTemplate("xhdpi", 5));
    }

    /**
     * Extracts icon file
     *
     * @return icons byte[], null if no icon
     * @throws ExtractingInfoException in case of error
     */
    public byte[] getIcon() {
        try {
            foundIconEntryName = null;
            foundIconPriority = 0;
            findBestIcon();
            return getFoundIcon();
        } catch (IOException e) {
            throw new ExtractingInfoException("icon extracting error", e);
        }
    }

    private void findBestIcon() throws IOException {
        final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(apkFile));
        ZipEntry zipentry = zipInputStream.getNextEntry();
        while (zipentry != null) {
            final String entryName = zipentry.getName();
            final long zipentrySize = zipentry.getSize();
            LOGGER.debug("reading entry: " + entryName);
            for (int newPriority = foundIconPriority + 1;
                 newPriority <= ICONS_PATH_MAX_PRIORITY;
                 newPriority++) {
                final IconPathTemplate template = iconsPathPriorityMap.get(newPriority);
                if (template != null) {
                    if (template.checkPath(entryName)) {
                        foundIconEntryName = entryName;
                        foundIconPriority = newPriority;
                        if (zipentrySize > Integer.MAX_VALUE) {
                            throw new IllegalStateException("size of that icon is too damn high: " + zipentrySize);
                        } else {
                            foundIconEntrySize = (int) zipentrySize;
                        }
                        break;
                    }
                }
            }
            zipentry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    private byte[] getFoundIcon() throws IOException {
        final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(apkFile));
        ZipEntry zipentry = zipInputStream.getNextEntry();
        while (zipentry != null) {
            final String entryName = zipentry.getName();
            LOGGER.debug("reading entry: " + entryName);
            if (StringUtils.equals(entryName, foundIconEntryName)) {
                final byte[] iconBytes = new byte[foundIconEntrySize];
                zipInputStream.read(iconBytes, 0, foundIconEntrySize);
                zipInputStream.close();
                return iconBytes;
            }
            zipentry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        LOGGER.debug("no icon found");
        return null;
    }

    private static class IconPathTemplate {
        private static final String STARTS_WITH = "res/drawable";
        private static final String ICON_NAME = "/icon.";
        private final String qualificator;
        private final int priority;

        public IconPathTemplate(final String qualificator, final int priority) {
            this.qualificator = qualificator;
            this.priority = priority;
        }

        public String getQualificator() {
            return qualificator;
        }

        public int getPriority() {
            return priority;
        }

        public boolean checkPath(final String path) {
            //TODO check if this enough
            return StringUtils.startsWith(path, STARTS_WITH)
                    && StringUtils.contains(path, qualificator)
                    && StringUtils.contains(path, ICON_NAME);
        }

    }
}
