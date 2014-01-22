package com.noveogroup.clap.web.resource;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.StreamedContent;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Resource handler for to allow using dynamic StreamedContent from
 * composite components and data tables
 *
 * @author Andrey Sokolov
 */
public class ELStreamedContentResourceHandler extends ResourceHandlerWrapper {

    public static final String EL_PARAM_KEY = "contentEL";

    private static final Set<String> ALLOWED_EL_PREFIXES = new HashSet<String>();

    static {
        ALLOWED_EL_PREFIXES.add("projectsModel.projectsListDataModel.getRowData");
        ALLOWED_EL_PREFIXES.add("revisionsModel.cleanPackageModel");
        ALLOWED_EL_PREFIXES.add("revisionsModel.hackedPackageModel");
    }

    private final ResourceHandler wrapped;

    public ELStreamedContentResourceHandler(final ResourceHandler original) {
        this.wrapped = original;
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }


    @Override
    public void handleResourceRequest(final FacesContext context) throws IOException {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String applyedEL = params.get(EL_PARAM_KEY);
        if (applyedEL != null) {
            final ExternalContext externalContext = context.getExternalContext();
            if (isELAllowed(applyedEL)) {
                final ELContext elContext = context.getELContext();
                final ValueExpression valueExpression = context.getApplication().getExpressionFactory()
                        .createValueExpression(elContext, "#{" + applyedEL + "}", StreamedContent.class);
                final StreamedContent streamedContent = (StreamedContent) valueExpression.getValue(elContext);

                externalContext.setResponseStatus(200);
                externalContext.setResponseContentType(streamedContent.getContentType());

                final byte[] buffer = new byte[2048];

                int length;
                final InputStream inputStream = streamedContent.getStream();
                while ((length = (inputStream.read(buffer))) > 0) {
                    externalContext.getResponseOutputStream().write(buffer, 0, length);
                }
            } else {
                externalContext.setResponseStatus(403);
            }
            externalContext.responseFlushBuffer();
            context.responseComplete();
        } else {
            getWrapped().handleResourceRequest(context);
        }

    }

    private boolean isELAllowed(final String el) {
        for (final String allowedPrefix : ALLOWED_EL_PREFIXES) {
            if (StringUtils.startsWith(el, allowedPrefix)) {
                return true;
            }
        }
        return false;
    }
}
