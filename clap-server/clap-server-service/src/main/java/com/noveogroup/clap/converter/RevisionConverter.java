package com.noveogroup.clap.converter;

import com.noveogroup.clap.entity.ProjectEntity;
import com.noveogroup.clap.entity.message.MessageEntity;
import com.noveogroup.clap.entity.revision.RevisionEntity;
import com.noveogroup.clap.model.message.Message;
import com.noveogroup.clap.model.revision.Revision;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;

/**
 * @author Andrey Sokolov
 */
public class RevisionConverter {

    private static final Mapper MAPPER = new DozerBeanMapper();

    public RevisionEntity map(Revision revision) {
        return MAPPER.map(revision, RevisionEntity.class);
    }

    public Revision map(RevisionEntity revision) {
        final Revision ret = new Revision();
        ret.setId(revision.getId());
        ret.setHash(revision.getHash());
        ret.setMessages(new ArrayList<Message>());
        for (MessageEntity message : revision.getMessages()) {
            ret.getMessages().add(MAPPER.map(message, Message.class));
        }
        final ProjectEntity project = revision.getProject();
        if(project != null){
            ret.setProjectId(project.getId());
        }
        ret.setRevisionType(revision.getRevisionType());
        ret.setTimestamp(revision.getTimestamp());
        return ret;
    }
}
