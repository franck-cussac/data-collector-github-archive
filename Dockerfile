FROM hseeberger/scala-sbt:11.0.13_1.5.8_2.13.7 as builder

WORKDIR /app

COPY build.sbt ./
COPY project/ project/
RUN sbt update

COPY src/ src/
RUN sbt assembly

FROM openjdk:11.0.13-slim-buster

WORKDIR /app

COPY --from=builder /app/target/scala-2.13/collect-data.jar /app/
CMD java -jar collect-data.jar
