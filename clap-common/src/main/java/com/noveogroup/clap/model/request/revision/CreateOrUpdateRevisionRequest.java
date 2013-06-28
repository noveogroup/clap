package com.noveogroup.clap.model.request.revision;

import com.noveogroup.clap.model.user.User;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: mdemidov
 * Date: 5/31/13
 * Time: 6:42 PM
 *
 * @since 5/31/13 6:42 PM
 */
public class CreateOrUpdateRevisionRequest {

    @FormParam("projectExternalId")
    @PartType("multipart/form-data")
    private String projectExternalId;

    @FormParam("revisionHash")
    @PartType("multipart/form-data")
    private String revisionHash;

    @FormParam("user")
    @PartType("application/json")
    private User user;

    @FormParam("mainPackage")
    @PartType("application/octet-stream")
    private InputStream mainPackage;

    @FormParam("specialPackage")
    @PartType("application/octet-stream")
    private InputStream specialPackage;

    public String getProjectExternalId() {
        return projectExternalId;
    }

    public void setProjectExternalId(final String projectExternalId) {
        this.projectExternalId = projectExternalId;
    }

    public String getRevisionHash() {
        return revisionHash;
    }

    public void setRevisionHash(final String revisionHash) {
        this.revisionHash = revisionHash;
    }

    public InputStream getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(final InputStream mainPackage) {
        this.mainPackage = mainPackage;
    }

    public InputStream getSpecialPackage() {
        return specialPackage;
    }

    public void setSpecialPackage(final InputStream specialPackage) {
        this.specialPackage = specialPackage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
