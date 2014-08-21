package com.noveogroup.clap.model.response;

import java.io.Serializable;

/**
 * @author Andrey Sokolov
 */
public class ClapResponse implements Serializable {

    public static final int ERROR_CODE_OK = 0;
    public static final int ERROR_CODE_TOKEN_EXPIRED = 1;
    public static final int ERROR_CODE_AUTH_FAILED = 2;
    public static final int ERROR_CODE_UNIDENTIFIED_ERROR = 3;
    public static final int ERROR_CODE_DATA_ERROR = 4;

    private int code;
    private String message;

    public ClapResponse() {
        code = ERROR_CODE_OK;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
