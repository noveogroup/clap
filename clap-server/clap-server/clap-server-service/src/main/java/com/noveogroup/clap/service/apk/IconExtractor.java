package com.noveogroup.clap.service.apk;

import com.noveogroup.clap.model.revision.ApkAndroidManifest;
import com.noveogroup.clap.model.revision.ApkEntry;
import com.noveogroup.clap.model.revision.ApkStructure;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

/**
 * @author Andrey Sokolov
 */
public class IconExtractor implements InfoExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(IconExtractor.class);

    private static final int ICONS_PATH_MAX_PRIORITY = 5;
    private final Map<Integer, IconPathTemplate> iconsPathPriorityMap;
    private String foundIconEntryName;
    private int foundIconPriority;
    private byte[] icon;

    public IconExtractor() {
        iconsPathPriorityMap = new HashMap<Integer, IconPathTemplate>();
        iconsPathPriorityMap.put(1, new IconPathTemplate("", 1));
        iconsPathPriorityMap.put(2, new IconPathTemplate("ldpi", 2));
        iconsPathPriorityMap.put(3, new IconPathTemplate("mdpi", 3));
        iconsPathPriorityMap.put(4, new IconPathTemplate("hdpi", 4));
        iconsPathPriorityMap.put(5, new IconPathTemplate("xhdpi", 5));
    }

    public byte[] getIcon() {
        return icon;
    }

    private void findBestIcon(final ApkStructure structure, final ApkAndroidManifest androidManifest) {
        final ApkEntry rootEntry = structure.getRootEntry();
        foundIconEntryName = null;
        foundIconPriority = 0;
        for (final IconPathTemplate template : iconsPathPriorityMap.values()) {
            //TODO fix
            template.setIconName(androidManifest.getIconPath());
        }
        searchIcon(rootEntry);
    }

    private void searchIcon(final ApkEntry apkEntry) {
        if (apkEntry != null) {
            if (apkEntry.isDirectory()) {
                for (final ApkEntry innerEntry : apkEntry.getInnerEntries()) {
                    searchIcon(innerEntry);
                }
            } else {
                for (int newPriority = foundIconPriority + 1;
                     newPriority <= ICONS_PATH_MAX_PRIORITY;
                     newPriority++) {
                    final IconPathTemplate template = iconsPathPriorityMap.get(newPriority);
                    if (template != null) {
                        if (template.checkPath(apkEntry.getEntryName())) {
                            foundIconEntryName = apkEntry.getEntryName();
                            foundIconPriority = newPriority;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isEntryInterested(final ZipEntry entry,
                                     final ApkStructure structure,
                                     final ApkAndroidManifest androidManifest) {
        if (foundIconEntryName == null) {
            findBestIcon(structure, androidManifest);
        }
        return StringUtils.equals(entry.getName(), foundIconEntryName);
    }

    @Override
    public void processEntry(final byte[] entryContent,
                             final ZipEntry apkEntry,
                             final ApkStructure structure,
                             final ApkAndroidManifest androidManifest) {
        icon = entryContent;
    }

    private static class IconPathTemplate {
        private static final String STARTS_WITH = "res/drawable";
        private static final String ICON_NAME = "/ic_launcher.";
        private final String qualificator;
        private final int priority;
        private String iconName;

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

        public String getIconName() {
            return iconName;
        }

        public void setIconName(final String iconName) {
            this.iconName = iconName;
        }

        public boolean checkPath(final String path) {
            //TODO check if this enough
            return StringUtils.startsWith(path, STARTS_WITH)
                    && StringUtils.contains(path, qualificator)
                    && StringUtils.contains(path, StringUtils.isNotBlank(iconName) ? iconName : ICON_NAME);
        }

    }
}
