package com.noveogroup.clap.web.util.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.Date;

public class TimestampConverter implements Converter {

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        //support only for output
        return null;
    }

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        final Long timestamp = (Long) o;
        return new Date(timestamp).toString();
    }
}
