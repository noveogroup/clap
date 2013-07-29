package com.noveogroup.clap.service.apk.manifest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Sokolov
 */
@XmlRootElement
public class Manifest {
    @XmlAttribute(name = "package")
    private String applicationPackage;

    @XmlElement(name = "uses-permission")
    private List<UsesPermisson> usesPermissons = new ArrayList<UsesPermisson>();
}
