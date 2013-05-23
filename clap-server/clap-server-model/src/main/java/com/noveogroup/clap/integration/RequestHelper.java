package com.noveogroup.clap.integration;

/**
 * interface to define request scoped helper bean
 * used by LightInterceptors
 *
 * @author Andrey Sokolov
 */
public interface RequestHelper {
    Class<? extends RequestHelper> getType();
}
