package com.zea.cloud.user.config;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.util.Set;

@Slf4j
@Configuration
public class NacosWarDeployConfig {

    @Resource
    private Environment env;
    @Resource
    private NacosRegistration registration;
    @Resource
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    @PostConstruct
    private void nacosServerRegister() {
        if (registration != null) {
            registration.setPort(getTomcatPort());
            nacosAutoServiceRegistration.start();
        }
    }

    public int getTomcatPort() {
        try {
            return getProvideTomcatPort();
        } catch (Exception e) {
            log.warn("obtain provide tomcat port failed, fallback to embed tomcat port.");
        }
        return env.getProperty("server.port", Integer.class, 8080);
    }

    private int getProvideTomcatPort() throws Exception {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(
                new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1"))
        );
        String port = objectNames.iterator().next().getKeyProperty("port");
        return Integer.parseInt(port);
    }
}
