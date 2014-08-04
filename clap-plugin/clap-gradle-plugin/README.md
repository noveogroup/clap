To use this plugin add to build.gradle following:

dependencies {
    %instrumentingVariant%Compile 'com.noveogroup.clap:clap-client:0.1-SNAPSHOT'
}

repositories {
    mavenLocal() //repo where aar will be published
}

buildscript {
    repositories {
        mavenLocal()//mavenCentral/NoveoNexus/etc. - where plugin will be published
    }
    dependencies {
        classpath 'com.noveogroup.clap:clap-gradle-plugin:0.1-SNAPSHOT'
    }
}
apply plugin: 'com.noveogroup.clap'

//clap plugin settings(will be extended)
clap {
    instrumenting = ['addLogs','sendLogs']  //all by default
    instrumentingVariants = ['debug']
    clapProjectId = "testProjectId"
}
