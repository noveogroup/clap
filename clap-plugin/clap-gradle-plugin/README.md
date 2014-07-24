To use this plugin add to build.gradle following:

buildscript {
    repositories {
        mavenLocal()//mavenCentral/NoveoNexus/etc. - where plugin will be published
    }
    dependencies {
        classpath group: 'com.noveogroup.clap', name: 'clap-gradle-plugin', version: '1.0-SNAPSHOT'
    }
}
apply plugin: 'clap'

//clap plugin settings(will be extended)
clap {
    instrumenting = ['addLogs','sendLogs']
    enableInstrumenting = true //shortcut to disable instrumenting
}
