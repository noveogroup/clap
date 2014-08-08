package com.noveogroup.clap.gradle.config

class Options {

    String projectId

    String serverUrl

    String username

    String password

    List<String> instrument

    void instrument(String... array) { instrument = array }

}
