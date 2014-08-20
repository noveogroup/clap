[CLAP](..) Server
=================

TODO: description

Old README file
===============

1. Put properties files clap.properties and clap-ui.properties before server-app deployment
2. Update your settings.xml with required properties, for example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <profiles>
    <profile>
      <id>clap-profile</id>
      <properties>
        <clap.serviceUrl>http://10.0.14.53:8080/clap-rest/v1</clap.serviceUrl>
        <clap.login>Uasya</clap.login>
        <clap.password>uasinPassword</clap.password>
      </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>clap-profile</activeProfile>
  </activeProfiles>
</settings>
```
3. To test revision and messages upload:
   - Check InitService to put role and permission that you want for you
   - Check "TODO" at com.noveogroup.clap.client.service.ClapService - build it with your own credentials, and server url
   - deploy clap-client on your android device
   - use mvn clean install clap:upload to build your test application and publish it on clap.
   - Enable library profile to get all things work
   - deploy test application on your android device(by adb or download from clap)
   - "crash" your test application

**NOTE**:
login matches your domain login(CAS integration),
but password is the one you put in your account settings on CLAP via web UI.
So you have no access by REST until you set it.
Don't set it as your domain password for security.

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
