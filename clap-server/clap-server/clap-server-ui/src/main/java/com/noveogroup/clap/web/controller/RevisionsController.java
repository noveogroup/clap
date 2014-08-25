package com.noveogroup.clap.web.controller;

import com.google.common.collect.Lists;
import com.noveogroup.clap.model.message.BaseMessage;
import com.noveogroup.clap.model.message.CrashMessage;
import com.noveogroup.clap.model.message.LogsBunchMessage;
import com.noveogroup.clap.model.message.ScreenshotMessage;
import com.noveogroup.clap.model.revision.ApkEntry;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionType;
import com.noveogroup.clap.model.revision.RevisionVariantWithApkStructure;
import com.noveogroup.clap.model.user.User;
import com.noveogroup.clap.service.project.ProjectService;
import com.noveogroup.clap.service.revision.RevisionService;
import com.noveogroup.clap.web.Navigation;
import com.noveogroup.clap.web.model.projects.ProjectsModel;
import com.noveogroup.clap.web.model.revisions.RevisionsListDataModel;
import com.noveogroup.clap.web.model.revisions.RevisionsModel;
import com.noveogroup.clap.web.model.user.UserSessionData;
import com.noveogroup.clap.web.util.message.MessageSupport;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Named
@RequestScoped
public class RevisionsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionsController.class);
    @Inject
    private ProjectsModel projectsModel;

    @Inject
    private RevisionsModel revisionsModel;

    @Inject
    private RevisionService revisionService;

    @Inject
    private ProjectService projectService;

    @Inject
    private UserSessionData userSessionData;

    @Inject
    private ProjectsController projectsController;

    @Inject
    private MessageSupport messageSupport;

    public void prepareRevisionView() throws IOException {
        final Revision selectedRevision = revisionsModel.getSelectedRevision();
        if (selectedRevision != null) {
            populateRevisionTypesList();
            LOGGER.debug(selectedRevision.getId() + " revision preparing finished");
        } else {
            LOGGER.error("revision not selected");
        }
    }

    public void prepareRevisionVariantView() throws IOException {
        final RevisionVariantWithApkStructure selectedRevisionVariant = revisionsModel.getSelectedRevisionVariant();
        if (selectedRevisionVariant != null) {
            revisionsModel.getSelectedRevCrashes().clear();
            revisionsModel.getSelectedRevScreenshots().clear();
            revisionsModel.getSelectedRevLogs().clear();
            for (final BaseMessage message : selectedRevisionVariant.getMessages()) {
                if (message instanceof CrashMessage) {
                    revisionsModel.getSelectedRevCrashes().add((CrashMessage) message);
                }
                if (message instanceof ScreenshotMessage) {
                    revisionsModel.getSelectedRevScreenshots().add((ScreenshotMessage) message);
                }
                if (message instanceof LogsBunchMessage) {
                    revisionsModel.getSelectedRevLogs().add((LogsBunchMessage) message);
                }
            }
            if (selectedRevisionVariant.getApkStructure() != null) {
                revisionsModel.setSelectedVariantApkStructure(
                        createApkStructureTree(null, selectedRevisionVariant.getApkStructure().getRootEntry()));
            }
            LOGGER.debug(selectedRevisionVariant.getId() + " revision preparing finished");
        } else {
            LOGGER.error("revision not selected");
        }
    }

    private TreeNode createApkStructureTree(final TreeNode root, final ApkEntry apkEntry) {
        final TreeNode ret = new DefaultTreeNode(apkEntry, root);
        final List<ApkEntry> innerEntries = apkEntry.getInnerEntries();
        if (innerEntries != null) {
            for (final ApkEntry innerEntry : innerEntries) {
                createApkStructureTree(ret, innerEntry);
            }
        }
        return ret;
    }

    public String removeSelectedRevision() {
        revisionService.deleteRevision(revisionsModel.getSelectedRevision().getId());
        return Navigation.PROJECT.getView();
    }

    public void removeRevision(final String revisionId) {
        long id = Long.parseLong(revisionId);
        revisionService.deleteRevision(id);
        revisionsModel.getRevisionsListDataModel().remove(id);
        redirectTo(Navigation.PROJECT);
    }

    public void prepareMyRevisionsView() {
        if (isAjaxRequest()) {
            return;
        }
        final User user = userSessionData.getUser();
        if (user != null) {
            final List<Revision> userRevisions = Lists.newArrayList();
            //TODO it
            revisionsModel.setRevisionsListDataModel(
                    new RevisionsListDataModel(userRevisions));
        } else {
            revisionsModel.setRevisionsListDataModel(
                    new RevisionsListDataModel(Collections.<Revision>emptyList()));
        }
    }

    public void populateRevisionTypesList() {
        final User user = userSessionData.getUser();
        final Revision selectedRevision = revisionsModel.getSelectedRevision();
        final Set<RevisionType> availableTypes = revisionService.getAvailableTypesToChange(user, selectedRevision);
        final List<SelectItem> types = Lists.newArrayList();
        for (RevisionType revisionType : availableTypes) {
            types.add(new SelectItem(revisionType, revisionType.name()));
        }
        revisionsModel.setRevisionTypes(types);
    }


    public void onRevisionTypeSelected() {
        revisionService.updateRevisionData(revisionsModel.getSelectedRevision());
        LOGGER.error("Type is : " + revisionsModel.getSelectedRevision().getRevisionType());
        messageSupport.addMessage("revisionControlMessages", "common.form.info.update.success");
    }
}
