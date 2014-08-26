package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.model.BaseModel;
import com.noveogroup.clap.web.util.message.MessageSupport;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseModelConverter implements Converter {

    @Inject
    private MessageSupport messageSupport;

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        if (!(o instanceof BaseModel) || ((BaseModel) o).getId() == null) {
            return null;
        }
        return String.valueOf(((BaseModel) o).getId());
    }


    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        if (s == null || !s.matches("\\d+")) {
            return null;
        }
        Object object = getObject(Long.valueOf(s));
        if (object == null) {
            final FacesMessage message = new FacesMessage(messageSupport.getMessage(getErrorMessageId(),
                    new Object[]{s}));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(message);
        }
        return object;
    }

    protected abstract Object getObject(Long id);

    protected abstract String getErrorMessageId();
}
