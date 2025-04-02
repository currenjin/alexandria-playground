# Spring-Data-Elasticsearch-POC
해당 프로젝트는 Spring Data Elasticsearch 에 대한 Index/Query POC(Proof of concept) 입니다.<br>

아래 서비스에 대한 POC 를 지원합니다.
- Elasticsearch
- Opensearch

<br>

This project is an Index/Query POC (Proof of Concept) for Spring Data Elasticsearch.<br>

We support POC for the following services:
- Elasticsearch
- Opensearch

## Diagram

<img width="1065" alt="image" src="https://user-images.githubusercontent.com/60500649/169252567-06400514-c093-4ac3-87b9-5c52dcc5ad4d.png">

## Run
### Clone project
```shell
$ https://github.com/currenjin/elasticsearch-poc
```

### Elasticsearch
`$ make start-elasticsearch` * jar is required.

### Opensearch
`$ make start-opensearch` * jar is required.

### Invoke api
> PORT Number
> - Elasticsearch client : 8081
> - Opensearch client : 8082

#### In(Index)
```shell
$ curl -d '{"id":"test","name":"currenjin","phoneNumber":"01012341234"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:{PORT}/
```

#### Out(Query)
```shell
$ curl -X GET "http://localhost:{PORT}/test"
$ curl -X GET "http://localhost:{PORT}?name=currenjin"
$ curl -X GET "http://localhost:{PORT}?phoneNumber=01012341234"
```
