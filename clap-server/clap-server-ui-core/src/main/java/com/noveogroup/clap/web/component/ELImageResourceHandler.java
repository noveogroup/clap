package com.noveogroup.clap.web.component;

import org.primefaces.model.StreamedContent;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
public class ELImageResourceHandler extends ResourceHandlerWrapper {

    public static final String EL_PARAM_KEY = "imageEL";

    private ResourceHandler wrapped;

    public ELImageResourceHandler(ResourceHandler original) {
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
            //TODO prevent EL injection
            final ELContext elContext = context.getELContext();
            final ValueExpression valueExpression = context.getApplication().getExpressionFactory()
                    .createValueExpression(elContext, "#{" + applyedEL + "}", StreamedContent.class);
            final StreamedContent streamedContent = (StreamedContent) valueExpression.getValue(elContext);

            final ExternalContext externalContext = context.getExternalContext();
            externalContext.setResponseStatus(200);
            externalContext.setResponseContentType(streamedContent.getContentType());

            final byte[] buffer = new byte[2048];

            int length;
            final InputStream inputStream = streamedContent.getStream();
            while ((length = (inputStream.read(buffer))) >= 0) {
                externalContext.getResponseOutputStream().write(buffer, 0, length);
            }

            externalContext.responseFlushBuffer();
            context.responseComplete();
        } else {
            getWrapped().handleResourceRequest(context);
        }

    }
}
