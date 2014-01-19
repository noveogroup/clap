package com.noveogroup.clap.integration;

/**
 * @author Andrey Sokolov
 */
public interface RequestHelperFactory {

    <T extends RequestHelper> T getRequestHelper(Class<? extends T> requestHelperClass);

    void addRequestHelper(RequestHelper requestHelper);
}
