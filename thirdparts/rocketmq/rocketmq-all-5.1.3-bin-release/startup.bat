set ROCKETMQ_HOME=D:\Code\Private\zea-cloud\thirdparts\rocketmq\rocketmq-all-5.1.3-bin-release
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_40
cd bin
start mqnamesrv.cmd -c ..\conf\namesrv.properties
start mqbroker.cmd -c ..\conf\broker.conf
