package com.noveogroup.clap.auth;

import com.noveogroup.clap.config.ConfigBean;
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
            systemId = DefaultAuthenticationSystem.SYSTEM_ID;
            LOGGER.warn("Prefered authentication system id not set in properties, using default CLAP authentication system");
        }
        if(authenticationSystems != null){
            for(AuthenticationSystem authenticationSystem : authenticationSystems){
                if(StringUtils.equals(systemId,authenticationSystem.getSystemId())){
                    selectedAuthenticationSystem = authenticationSystem;
                }
            }
        } else {
            selectedAuthenticationSystem = new DefaultAuthenticationSystem();
        }
    }

    public AuthenticationSystem getAuthenticationSystem(){
        return selectedAuthenticationSystem;
    }
}
