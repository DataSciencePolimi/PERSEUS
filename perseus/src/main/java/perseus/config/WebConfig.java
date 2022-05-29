package perseus.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import perseus.Application;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          	.addResourceHandler("/webjars/**")
          	.addResourceLocations("/webjars/");
		registry
			.addResourceHandler("/caseStudiesPictures/**")
			.addResourceLocations("file:" + Application.IMG_SC);
    }
    
    @Bean
    public ServletWebServerFactory getTomcatServletWebServerFactory() {
        final TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                final SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                final SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        serverFactory.addAdditionalTomcatConnectors(getHttpConnector());
        return serverFactory;
    }
    
    private Connector getHttpConnector() {
        final Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(80);
        connector.setSecure(false);
        connector.setRedirectPort(443);
        return connector;
    }
}