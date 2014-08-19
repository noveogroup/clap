package com.noveogroup.clap.web.util.converter;

import com.noveogroup.clap.model.user.ClapPermission;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Andrey Sokolov
 */
public class ClapPermissionConverter implements Converter {
    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        return ClapPermission.valueOf(s);
    }

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        return ((ClapPermission)o).name();
    }
}
