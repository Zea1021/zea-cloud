@echo off
timeout /t 1

cd /d D:\Code\Private\zea-cloud\thirdparts\nacos-2.2.1\bin
start  startup.cmd
timeout /t 5

cd /d D:\Code\Private\zea-cloud\thirdparts\redis
start redis-server-start.bat
timeout /t 1

cd /d D:\Code\Private\zea-cloud\thirdparts\minio
start startMinio.bat
timeout /t 1

cd /d D:\Code\Private\zea-cloud\thirdparts\xxl-job
start  startup.bat
timeout /t 1

cd /d D:\Code\Private\zea-cloud\thirdparts\rocketmq\rocketmq-all-5.1.3-bin-release
start  startup.bat
timeout /t 1

cd /d D:\Code\Private\zea-cloud\thirdparts\seata-1.6.1\bin
start  seata-server.bat
timeout /t 1

:pause