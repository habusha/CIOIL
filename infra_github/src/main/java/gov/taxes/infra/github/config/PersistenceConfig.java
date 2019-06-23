package gov.taxes.infra.github.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author Dan Erez
 *
 *         JPA Configuration.
 */
@Configuration
@EnableJpaRepositories(basePackages = { "gov.taxes.**.repository" })
@EntityScan(basePackages = { "gov.taxes.**.entity" })
@EnableTransactionManagement
public class PersistenceConfig {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}

}