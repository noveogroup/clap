package com.noveogroup.clap.service.file;

import java.io.File;
import java.io.InputStream;

/**
 * service to work with temporary files
 *
 * @author Andrey Sokolov
 */
public interface FileService {

    /**
     * save file on FS(won't be cleared automatically)
     *
     * @param content to place in file
     * @return file with written content
     */
    File saveFile(InputStream content);

    File saveFile(InputStream content,String namePrefix);

    /**
     *
     * @param path
     * @return null if not exists
     */
    File getFile(String path);

    boolean removeFile(String path);

    /**
     * creates new temp file and write input stream in it
     *
     * @param content to place in temp file
     * @return temp file with written content
     */
    File createTempFile(InputStream content);
}
