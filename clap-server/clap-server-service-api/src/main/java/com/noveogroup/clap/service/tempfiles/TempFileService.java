package com.noveogroup.clap.service.tempfiles;

import java.io.File;
import java.io.InputStream;

/**
 * service to work with temporary files
 *
 * @author Andrey Sokolov
 */
public interface TempFileService {

    /**
     * creates new temp file and write input stream in it
     *
     * @param content to place in temp file
     * @return temp file with written content
     */
    File createTempFile(InputStream content);
}
