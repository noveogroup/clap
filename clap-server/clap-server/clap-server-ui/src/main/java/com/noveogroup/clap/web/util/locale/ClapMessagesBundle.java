package com.noveogroup.clap.web.util.locale;

/**
 * @author Andrey Sokolov
 */
public class ClapMessagesBundle extends MessagesBundle {

    //refers to bundle
    protected static final String BUNDLE_NAME = "messages";

    @Override
    protected String getBundleName() {
        return BUNDLE_NAME;
    }
}
