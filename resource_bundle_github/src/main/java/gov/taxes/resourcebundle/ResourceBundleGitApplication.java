package gov.taxes.resourcebundle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import gov.taxes.infra.github.annotation.InheritedComponent;
import gov.taxes.infra.github.utils.TaxesUtils;

@SpringBootApplication
@EnableEurekaClient
@EnableEncryptableProperties
@EnableCaching
@ComponentScan(basePackages = TaxesUtils.TAXES_PACKAGE, includeFilters = {
		@Filter(type = FilterType.ANNOTATION, classes = InheritedComponent.class) })
public class ResourceBundleGitApplication {

	public static void main(String[] arguments) {
		SpringApplication.run(ResourceBundleGitApplication.class, arguments);
	}
	
}
