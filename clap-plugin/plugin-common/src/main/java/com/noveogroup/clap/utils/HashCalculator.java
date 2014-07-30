package com.noveogroup.clap.utils;

import org.codehaus.plexus.util.DirectoryWalkListener;
import org.codehaus.plexus.util.DirectoryWalker;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//TODO logging
public class HashCalculator {

    private static final String ALPHABET = "0123456789ABCDEF";

    private DirectoryWalker directoryWalker;


    public HashCalculator(final File baseDir, final String[] include, final String[] exclude) {
        directoryWalker = new DirectoryWalker();
        directoryWalker.setBaseDir(baseDir);
        if (include != null) {
            for (String file : include) {
                directoryWalker.addInclude(file);
            }
        }
        if (exclude != null) {
            for (String file : exclude) {
                directoryWalker.addExclude(file);
            }
        }
    }

    public String calculateHash(final String checksumAlgorithm) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(checksumAlgorithm);
            DirectoryWalkListener directoryWalkListener = new WalkListener(messageDigest);
            directoryWalker.addDirectoryWalkListener(directoryWalkListener);
            directoryWalker.setDebugMode(true);
            directoryWalker.scan();

            return getHexHash(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            //logger.error("Error while creating message digest " + e.getMessage(), e);
        } catch (Exception e) {
            //logger.error("Error while calculating hash " + e.getMessage(), e);
        }
        return null;
    }

    private class WalkListener implements DirectoryWalkListener {

        private MessageDigest messageDigest;

        public WalkListener(final MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @Override
        public void directoryWalkStarting(final File basedir) {

        }

        @Override
        public void directoryWalkStep(final int percentage, final File file) {
            //logger.info("Please wait: " + percentage + "%");
            if (file.isFile()) {
                try {
                    byte[] data = IOUtil.toByteArray(new FileInputStream(file));
                    messageDigest.update(data);
                } catch (IOException e) {
                    //logger.error("Error while hashing directory " + e.getMessage(), e);
                }
            }
        }

        @Override
        public void directoryWalkFinished() {

        }

        @Override
        public void debug(final String message) {
            //logger.info(message);
        }
    }

    private String getHexHash(final MessageDigest messageDigest) {
        byte[] bytes = messageDigest.digest();
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            sb.append(ALPHABET.charAt((b & 0xF0) >> 4));
            sb.append(ALPHABET.charAt((b & 0x0F)));
        }
        return sb.toString();
    }

}
