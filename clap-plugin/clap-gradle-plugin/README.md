Clap Gradle Plugin
==================

gradle plugin for instrumentation and uploading apk to CLAP system

Downloads
---------

TODO


Gradle setup
============

Add to build.gradle following:

```
dependencies {
    instrumentingVariantCompile 'com.noveogroup.clap:clap-client:0.1-SNAPSHOT'// in most cases "debugCompile" is enough
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

clap {
    instrumenting.variantName = ['addLogs','sendLogs']  //all by default
    instrumenting.flavor1Debug = ['sendLogs']
    //.....
    instrumentingVariants = ['variantName','flavor1Debug','flavor2Debug','etc.'] //['debug'] by default
    clapProjectId = "%clap project id%" //should be unique for different projects
}
```

If you specify instrumenting functionality, you need to put full variant name, eg: you have flavor1 and flavor2 - you need to put both
```
instrumenting.flavor1Debug = ['sendLogs']
instrumenting.flavor2Debug = ['sendLogs']
```
combining as follows WON'T WORK
```
instrumenting.debug = ['sendLogs']
```


Possible instrumenting functionality:
* addLogs - adding SLF4J logs for lifecycle method calls
* sendLogs - sending logs to CLAP(by bunches and at force-close case)


Developed By
============

* [Noveo Group](http://noveogroup.com/)
* [Andrey Sokolov](https://github.com/RagnarNSK) - <asokolov@noveogroup.com>
* [Mikhail Demidov]() - <mdemidov@noveogroup.com>
* [Pavel Stepanov](https://github.com/stefan-nsk) - <pstepanov@noveogroup.com>

License
=======

 Copyright (c) 2013 Noveo Group

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    Except as contained in this notice, the name(s) of the above copyright holders
    shall not be used in advertising or otherwise to promote the sale, use or
    other dealings in this Software without prior written authorization.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

