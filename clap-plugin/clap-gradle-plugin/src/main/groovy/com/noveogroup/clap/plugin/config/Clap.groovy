package com.noveogroup.clap.plugin.config

import org.apache.commons.lang.builder.ToStringBuilder

/**
 * @author Andrey Sokolov
 */
class Clap {
    //TODO final default value
    String serviceUrl = "http://10.0.14.53:8080/clap-rest"
    String clapProjectId

    //TODO check how to properly get auth
    String clapLogin = "test"
    String clapPassword = "123"

    HashCalculatorConfig hashCalculatorConfig = new HashCalculatorConfig()

    boolean enableInstrumenting = true
    Collection<String> instrumenting = ['addLogs','sendLogs']
    Collection<String> instrumentingVariants = ['debug']

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("serviceUrl", serviceUrl)
                .append("clapProjectId", clapProjectId)
                .append("clapLogin", clapLogin)
                .append("clapPassword", clapPassword)
                .append("hashCalculatorConfig", hashCalculatorConfig)
                .append("enableInstrumenting", enableInstrumenting)
                .append("instrumenting", instrumenting)
                .toString();
    }
}
