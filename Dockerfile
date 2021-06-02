# docker login visualorbit.azurecr.io
# or az acr login --name visualorbit
# docker build -t visualorbit.azurecr.io/visualorbit/uitests:latest .
# docker push visualorbit.azurecr.io/visualorbit/uitests:latest



FROM maven:3.6.2-jdk-11

WORKDIR /app

RUN chmod go+w /app

COPY docker/mvn-global-settings.xml /usr/share/maven/conf/settings.xml
COPY src/ ./src
COPY pom.xml ./pom.xml 

RUN mvn dependency:go-offline de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

