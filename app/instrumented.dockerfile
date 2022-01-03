FROM jacoco-control-poc_web

COPY jacoco /srv/server/jacoco

ENTRYPOINT ["java", "-jar", "/srv/server/app.jar", "-javaagent:jacoco/jacocoagent.jar=dumponexit=false,output=tcpserver,address=localhost,port=${JACOCO_AGENT_PORT},excludes=*"]
