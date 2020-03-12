FROM clojure

RUN mkdir -p /usr/src/app
COPY . /usr/src/app
WORKDIR /usr/src/app

RUN lein ring uberjar

CMD ["java", "-jar", "target/amazestats.jar"]
