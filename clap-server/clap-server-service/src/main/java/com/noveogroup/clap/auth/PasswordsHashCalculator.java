package com.noveogroup.clap.auth;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Andrey Sokolov
 */
public final class PasswordsHashCalculator {


    private PasswordsHashCalculator() {
        throw new UnsupportedOperationException("instantiate util class");
    }

    public static String calculatePasswordHash(final String password) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("empty password");
        }
        try {
            final MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = password.getBytes();
            m.update(data, 0, data.length);
            final BigInteger i = new BigInteger(1, m.digest());
            final String calculatedHash = i.toString(16);
            return calculatedHash;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("impossibru!", e);
        }
    }
}
