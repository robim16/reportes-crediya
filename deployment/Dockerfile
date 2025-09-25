FROM eclipse-temurin:21-jre
WORKDIR /app

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY applications/app-service/build/libs/reportes-crediya.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]

