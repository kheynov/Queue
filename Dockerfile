FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY build/libs/*.jar /app/queue.jar
ENTRYPOINT ["java","-jar","/app/queue.jar"]