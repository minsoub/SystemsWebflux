### SSL certificate problem: unable to get local issuer certificate
아래의 명령어를 통해 해당 프로젝트에서만 확인을 건너뛰게 할 수 있다.   
```shell script
git config --local http.sslVerify false
```