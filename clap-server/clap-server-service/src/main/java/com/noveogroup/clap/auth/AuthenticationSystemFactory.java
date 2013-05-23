package com.noveogroup.clap.auth;

import com.noveogroup.clap.config.ConfigBean;
import com.noveogroup.clap.integration.auth.AuthenticationSystem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author Andrey Sokolov
 */
@ApplicationScoped
public class AuthenticationSystemFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSystemFactory.class);

    public static final String DEFAULT_SYSTEM_ID = "DEFAULT";

    @Inject
    private ConfigBean configBean;

    @Inject
    @Any
    private Instance<AuthenticationSystem> authenticationSystems;

    private AuthenticationSystem selectedAuthenticationSystem;

    @PostConstruct
    protected void init(){
        String systemId = configBean.getAuthenticationSystemId();
        if(StringUtils.isBlank(systemId)){
            systemId = DEFAULT_SYSTEM_ID;
            LOGGER.warn("Prefered authentication system id not set in properties, "
                    + DEFAULT_SYSTEM_ID + " authentication system will be selected");
        }
        if(authenticationSystems != null){
            for(final AuthenticationSystem authenticationSystem : authenticationSystems){
                if(StringUtils.equals(systemId,authenticationSystem.getSystemId())){
                    selectedAuthenticationSystem = authenticationSystem;
                    LOGGER.info("Selected authentication system : " + selectedAuthenticationSystem.getSystemId());
                    return;
                }
            }
        } else {
            throw new IllegalStateException("package corrupted: missing DefaultAuthenticationSystem");
        }
    }

    public AuthenticationSystem getAuthenticationSystem(){
        return selectedAuthenticationSystem;
    }
}
