**stock-exchange-api** is a sample Spring Boot application which simulates the purchase of stocks.

Prerequisites
=============

Make sure you have a Java 11 JRE installed, and the `JAVA_HOME` environment variable points to this JRE.

How do I run it?
================

1. Open a command prompt or terminal window.
2. Navigate to the stock-exchange-api root directory.
3. Execute this command to build the artifact.

```
$ ./gradlew bootRun
```

You can use this Postman collection to interact with the API: [stock-exchange-api.postman_collection.json](docs/stock-exchange-api.postman_collection.json)

Details
=======

This application runs an in-memory database using [Apache Derby](https://db.apache.org/derby/). This means that data is only persisted while the application runs, and will disappear after the application stops.

Credits
=======

This application uses a library, [iextrading4j](https://github.com/WojciechZankowski/iextrading4j), to fetch stock information from a [free public API](https://iextrading.com/developer/docs/#getting-started) by IEX Trading.
