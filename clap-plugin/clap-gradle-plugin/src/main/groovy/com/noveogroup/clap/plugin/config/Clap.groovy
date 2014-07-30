package com.noveogroup.clap.plugin.config

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

    HashCalculatorConfig hashCalculatorConfig

    boolean enableInstrumenting
    Collection<String> instrumenting
}
