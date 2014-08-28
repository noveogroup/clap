package com.noveogroup.clap.web.util.converter;

import com.google.gson.Gson;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Andrey Sokolov
 */
public class ToJSONConverter implements Converter{

    private static final Gson gson = new Gson();

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        return null;
    }

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        return gson.toJson(o);
    }
}
