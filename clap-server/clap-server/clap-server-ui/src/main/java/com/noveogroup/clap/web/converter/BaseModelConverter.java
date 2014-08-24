package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.model.BaseModel;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * @author Andrey Sokolov
 */
public abstract class BaseModelConverter implements Converter {

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
            throw new ConverterException(new FacesMessage("Unknown ID: " + s));
        }
        return object;
    }

    protected abstract Object getObject(Long id);
}
