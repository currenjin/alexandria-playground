# Sleuth AWS SQS POC

해당 프로젝트는 Slueth(Zipkin) 를 통한 메시지 추적 POC(Proof of concept) 입니다.<br>
Rest API 를 통해 POST 요청을 받으면, 메시지를 발행/소비 합니다. 이 과정에서 유지되는 TraceId 를 확인합니다.<br>
<br>

This project is a Proof of Concept that tracks messages through Sleuth(Zipkin).<br>
When a POST request is received through the Rest API, the message is published/consumed. Check the TraceId maintained during this process.<br>
<br>

## Diagram
<img width="1244" alt="image" src="https://user-images.githubusercontent.com/60500649/165676051-07aed263-8549-4e59-88da-730f4420f431.png">

## Run
### Clone project
```
$ git clone https://github.com/currenjin/sleuth-sqs-poc
```

### Run container
```
$ make start-app
```

*jar is required.

### Invoke api
```
$ curl --request POST --data 'test' http://localhost:5000/post
```
