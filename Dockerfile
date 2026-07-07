FROM tomcat:9.0
COPY target/hello-cicd-web.war /usr/local/tomcat/webapps/
