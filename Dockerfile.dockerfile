FROM tomcat:9.0-jre8-alpine
ADD \target\Tasker-0.1.war
EXPOSE 8080
CMD["catalina.sh","run"]