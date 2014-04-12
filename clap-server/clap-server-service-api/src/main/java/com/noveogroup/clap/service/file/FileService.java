package com.noveogroup.clap.service.file;

import com.noveogroup.clap.model.file.FileType;

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
     * @param fileType to choose folder to save file(temp,apk,screenshot...)
     * @param content to place in file
     * @return file with written content
     */
    File saveFile(FileType fileType,InputStream content);

    File saveFile(FileType fileType,InputStream content,String namePrefix);

    /**
     *
     * @param path
     * @return null if not exists
     */
    File getFile(String path);

    boolean removeFile(String path);

}
