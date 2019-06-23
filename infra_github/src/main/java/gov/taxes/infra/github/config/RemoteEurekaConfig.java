package gov.taxes.infra.github.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * This class modifies the service discovery credentials to enable running it locally
 * against a remote discovery (with other instance of this service) by giving it a unique name. 
 * 
 * @author Dan Erez
 */
@Configuration
public class RemoteEurekaConfig {
	
	protected static Logger logger = LoggerFactory.getLogger(RemoteEurekaConfig.class);

	@Value("${dev.discovery:false}")
    private Boolean devDiscovery;
	
	@Value("${spring.application.name}")
	private String appName;

	@Value("${server.port}")
	private Integer port;

	EurekaInstanceConfigBean config;

	@Bean
	@Autowired
	public EurekaInstanceConfigBean eurekaInstanceConfigBean(final InetUtils inetUtils) {
		String newAppName = getHostname() + "." + appName;
		config = new EurekaInstanceConfigBean(inetUtils) {
			@Override
			public void setEnvironment(Environment environment) {
				super.setEnvironment(environment);
				if (devDiscovery != null && devDiscovery == true) {
					setAppname(newAppName);
					setVirtualHostName(newAppName);
					setSecureVirtualHostName(newAppName);
				}
			}
		};
		config.setNonSecurePort(port);
		config.setIpAddress(getHostAddress());
		config.getMetadataMap().put("instanceId", config.getHostname() + ":" + config.getAppname() + ":" + port);

		return config;
	}

	private static String getHostAddress() {
		String hostAddress = null;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("Cannot get host info", e);
		}
		return hostAddress;
	}

	private static String getHostname() {
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.error("Cannot get host info", e);
		}
		return hostname;
	}

}
