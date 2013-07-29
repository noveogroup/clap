package com.noveogroup.clap.service.apk.manifest;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Andrey Sokolov
 */
public class UsesPermisson {

    @XmlAttribute(name = "android:name")
    private String name;
}
