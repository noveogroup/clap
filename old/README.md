1) Put properties files clap.properties and clap-ui.properties before server-app deployment
2) Update your settings.xml with required properties, for example:

<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">


  <profiles>
    <profile>
      <id>clap-profile</id>
      <properties>
        <clap.serviceUrl>http://10.0.14.53:8080/clap-rest</clap.serviceUrl>
        <clap.login>Uasya</clap.login>
        <clap.password>uasinPassword</clap.password>
      </properties>
    </profile>
  </profiles>


  <activeProfiles>
    <activeProfile>clap-profile</activeProfile>
  </activeProfiles>
</settings>

3) To test revision and messages upload:
-) Check InitService to put role and permission that you want for you
-) Check "TODO" at com.noveogroup.clap.client.service.ClapService - build it with your own credentials, and server url
a) deploy clap-client on your android device
b) use mvn clean install clap:upload to build your test application and publish it on clap.
 Enable library profile to get all things work
c) deploy test application on your android device(by adb or download from clap)
d) "crash" your test application

NOTE: login matches your domain login(CAS integration),
 but password is the one you put in your account settings on CLAP via web UI.
 So you have no access by REST until you set it.
 Don't set it as your domain password for security.