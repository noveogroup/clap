package com.noveogroup.clap.web.util.message;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author Andrey Sokolov
 */
public final class MessageUtils {

    public static final String DEFAULT_BUNDLE_VAR = "msgs";

    private MessageUtils() {
        throw new UnsupportedOperationException("instantiate util class");
    }

    public static String getMessage(final FacesContext context, final String messageName) {
        return getMessage(context, DEFAULT_BUNDLE_VAR, messageName, null);
    }

    public static String getMessage(final FacesContext context, final String messageName, final Object[] args) {
        return getMessage(context, DEFAULT_BUNDLE_VAR, messageName, args);
    }

    public static String getMessage(final FacesContext context, final String bundleVar, final String messageName) {
        return getMessage(context, bundleVar, messageName, null);
    }

    public static String getMessage(final FacesContext context, final String bundleVar,
                                    final String messageName, final Object[] args) {


        final ValueExpression valueExpression = context.getApplication().getExpressionFactory()
                .createValueExpression(context.getELContext(), "#{" + bundleVar + "}", ResourceBundle.class);

        final ResourceBundle bundle = (ResourceBundle) valueExpression.getValue(context.getELContext());
        final String message = bundle.getString(messageName);
        if (args != null) {
            return new MessageFormat(message).format(args, new StringBuffer(), null).toString();
        } else {
            return message;
        }
    }
}
