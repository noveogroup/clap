package com.noveogroup.clap.service.messages;

import com.noveogroup.clap.dao.MessageDAO;
import com.noveogroup.clap.dao.ProjectDAO;
import com.noveogroup.clap.dao.RevisionDAO;
import com.noveogroup.clap.entity.Project;
import com.noveogroup.clap.entity.message.Message;
import com.noveogroup.clap.entity.revision.Revision;
import com.noveogroup.clap.interceptor.TransactionInterceptor;
import com.noveogroup.clap.interceptor.Transactional;
import com.noveogroup.clap.model.message.MessageDTO;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({TransactionInterceptor.class})
public class MessagesServiceImpl implements MessagesService {

    private static Mapper MAPPER = new DozerBeanMapper();

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private RevisionDAO revisionDAO;

    @EJB
    private MessageDAO messageDAO;

    @Transactional
    @Override
    public void saveMessage(String projectName, long revisionTimestamp, MessageDTO messageDTO) {
        Project project = projectDAO.findProjectByName(projectName);
        for(Revision revision : project.getRevisions()){
            if(revision.getTimestamp().getTime() == revisionTimestamp){
                Message message = MAPPER.map(messageDTO,Message.class);
                message = messageDAO.persist(message);
                List<Message> messages = revision.getMessages();
                if(messages == null){
                    messages = new ArrayList<Message>();
                    revision.setMessages(messages);
                }
                messages.add(message);
                revisionDAO.persist(revision);
                projectDAO.persist(project);
            }
        }
    }
}
