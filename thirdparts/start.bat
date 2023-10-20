set PROJECT_PATH=D:\Code\Private\zea-cloud

cd %PROJECT_PATH%\thirdparts\nacos-2.2.1\bin
start  startup.cmd
timeout /t 5

cd %PROJECT_PATH%\thirdparts\redis
start redis-server-start.bat
timeout /t 1

cd %PROJECT_PATH%\thirdparts\minio
start startMinio.bat
timeout /t 1

cd %PROJECT_PATH%\thirdparts\xxl-job
start  startup.bat
timeout /t 1

cd %PROJECT_PATH%\thirdparts\rocketmq\rocketmq-all-5.1.3-bin-release
start  startup.bat
timeout /t 1

cd %PROJECT_PATH%\thirdparts\seata-1.6.1\bin
start  seata-server.bat
timeout /t 1

:pause