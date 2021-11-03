FROM petsearch:latest

COPY ./file-storage /file-storage
COPY ./ml /ml

ENV SPRING_PROFILES_ACTIVE=prod,api-docs
ENV MANAGEMENT_METRICS_EXPORT_PROMETHEUS_ENABLED=true
ENV JHIPSTER_SLEEP=30
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://84.252.142.185:5432/petsearch
ENV SPRING_LIQUIBASE_URL=jdbc:postgresql://84.252.142.185:5432/petsearch


EXPOSE 80

CMD java -Xmx512m -Xms256m -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.kowechka.petsearch.PetsearchApp"  "$@"
