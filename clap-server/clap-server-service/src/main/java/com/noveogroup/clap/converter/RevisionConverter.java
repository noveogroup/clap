package com.noveogroup.clap.converter;

import com.google.gson.Gson;
import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.message.Message;
import com.noveogroup.clap.model.revision.ApkStructure;
import com.noveogroup.clap.model.revision.Revision;
import com.noveogroup.clap.model.revision.RevisionWithApkStructure;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;

/**
 * @author Andrey Sokolov
 */
public class RevisionConverter {

    private static final Mapper MAPPER = new DozerBeanMapper();

    public RevisionEntity map(final Revision revision) {
        return MAPPER.map(revision, RevisionEntity.class);
    }

    public Revision map(final RevisionEntity revision) {
        final Revision ret = new Revision();
        map(ret,revision);
        return ret;
    }

    public RevisionWithApkStructure mapWithApkStructure(final RevisionEntity revision) {
        final RevisionWithApkStructure ret = new RevisionWithApkStructure();
        map(ret, revision);
        ret.setApkStructure(new Gson().fromJson(revision.getApkStructureJSON(), ApkStructure.class));
        return ret;
    }


    private void map(final Revision toMap, final RevisionEntity revision) {
        toMap.setId(revision.getId());
        toMap.setHash(revision.getHash());
        toMap.setMessages(new ArrayList<Message>());
        for (final MessageEntity message : revision.getMessages()) {
            toMap.getMessages().add(MAPPER.map(message, Message.class));
        }
        final ProjectEntity project = revision.getProject();
        if(project != null){
            toMap.setProjectId(project.getId());
        }
        toMap.setRevisionType(revision.getRevisionType());
        toMap.setTimestamp(revision.getTimestamp());
    }
}