package com.noveogroup.clap.service.apk;

import com.noveogroup.clap.model.revision.ApkAndroidManifest;
import com.noveogroup.clap.service.apk.manifest.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Andrey Sokolov
 */
public class ManifestInfoExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestInfoExtractor.class);
    private static final String MANIFEST_NAME = "AndroidManifest.xml";

    //TODO it all
    private ApkAndroidManifest foundManifest;

    public ManifestInfoExtractor() {
    }

    void getManifest(final ZipEntry zipEntry, final ZipInputStream zipInputStream) {
        if (MANIFEST_NAME.equals(zipEntry.getName())) {
            if (foundManifest == null) {
                LOGGER.debug("reading manifest");
                try {
                    //assuming manifest can't be such large to break it
                    final byte[] content = new byte[(int) zipEntry.getSize()];
                    zipInputStream.read(content, 0, (int) zipEntry.getSize());
                    final JAXBContext jaxbContext = JAXBContext.newInstance(Manifest.class);
                    final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    final Manifest manifest = (Manifest) unmarshaller.unmarshal(new ByteArrayInputStream(content));
                    LOGGER.debug("manifest : " + manifest);
                    //TODO it
                    foundManifest = new ApkAndroidManifest();
                    foundManifest.setIconPath("/icon.");

                } catch (JAXBException e) {
                    LOGGER.error("parsing error", e);
                } catch (IOException e) {
                    LOGGER.error("reading from apk error", e);
                }
            } else {
                LOGGER.debug("manifest already read");
            }
        }
    }

    public ApkAndroidManifest getFoundManifest() {
        return foundManifest;
    }
}
