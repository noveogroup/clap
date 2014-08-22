package com.noveogroup.clap.web.converter;

import com.noveogroup.clap.model.BaseModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

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
}
