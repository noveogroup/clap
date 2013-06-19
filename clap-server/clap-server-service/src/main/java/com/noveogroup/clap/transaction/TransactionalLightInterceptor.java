package com.noveogroup.clap.transaction;

import com.noveogroup.clap.interceptor.composite.AbstractLightInterceptor;
import com.noveogroup.clap.interceptor.composite.LightInterceptor;
import com.noveogroup.clap.interceptor.composite.RequestHelperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.InvocationContext;
import javax.transaction.UserTransaction;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class TransactionalLightInterceptor extends AbstractLightInterceptor implements LightInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionalLightInterceptor.class);

    @Override
    public Object proceed(final InvocationContext context,
                          final RequestHelperFactory requestHelperFactory,
                          final Map<Class<? extends Annotation>, Annotation> annotationMap) throws Exception {
        Object result = null;
        if (annotationMap.containsKey(Transactional.class)) {
            final TransactionRequestHelper requestHelper = requestHelperFactory
                    .getRequestHelper(TransactionRequestHelper.class);
            final UserTransaction userTransaction = requestHelper.getUserTransaction();
            try {
                userTransaction.begin();
                result = nextInterceptor.proceed(context, requestHelperFactory, annotationMap);
                userTransaction.commit();
                return result;
            } catch (Exception e) {
                LOGGER.error("Transaction error " + e.getMessage(), e);
                userTransaction.rollback();
                throw e;
            }
        }
        try {
            result = nextInterceptor.proceed(context, requestHelperFactory, annotationMap);
        } catch (Exception e) {
            LOGGER.error("error beyond transaction " + e.getMessage(), e);
            throw e;
        }
        return result;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public String getDescription() {
        return "TransactionalLightInterceptor";
    }
}
