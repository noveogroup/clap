package com.noveogroup.clap.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Andrey Sokolov
 */
public final class ELUtils {

    private ELUtils(){
    }


    public static String urlEncode(final String param) throws UnsupportedEncodingException {
        return URLEncoder.encode(param, "UTF-8");
    }
}
