package com.noveogroup.clap.service.apk;

import com.noveogroup.clap.service.apk.exception.ExtractingInfoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Andrey Sokolov
 */
public class IconExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(IconExtractor.class);

    private final ZipInputStream zipInputStream;

    IconExtractor(ZipInputStream zipInputStream){
        this.zipInputStream = zipInputStream;
    }

    /**
     * Extracts icon file
     *
     * @return icons byte[], null if no icon
     *
     * @throws ExtractingInfoException in case of error
     */
    public byte[] getIcon(){
        try {
            ZipEntry zipentry = zipInputStream.getNextEntry();
            while (zipentry != null)
            {
                final String entryName = zipentry.getName();
                LOGGER.debug("reading entry: " + entryName);
                //final File newFile = new File(entryName);
                //final String directory = newFile.getParent();

                zipentry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            throw new ExtractingInfoException("icon extracting error",e);
        }
        LOGGER.debug("no icon found");
        return null;
    }
}
