FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -q -DskipTests -pl product-service -am clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/product-service/target/*.jar /app/

RUN ls -lah /app && \
    # Prefer the larger executable JAR (without version number) or the one with the version but not 'original'
    JAR=$(ls /app/*.jar | grep -v 'original' | sort -r | head -n 1) && \
    mv "$JAR" /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
