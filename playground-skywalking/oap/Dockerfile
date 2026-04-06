FROM apache/skywalking-oap-server:10.1.0
RUN mkdir -p /skywalking/ext-libs && \
    curl -L https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar \
    -o /skywalking/ext-libs/mysql-connector-j.jar
