package br.com.emerson;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.actuate.system.EmbeddedServerPortFileWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources({
    @PropertySource(value = "classpath:application.properties"),
    @PropertySource(value = "file:/tmp/application.properties")
})
@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource({"spring/application-context.xml"})
public class Application {

    @Value("${web.path}")
    private String servicePath;

    @Bean
    public ServletRegistrationBean cxfServlet() {
        org.apache.cxf.transport.servlet.CXFServlet cxfServlet = new org.apache.cxf.transport.servlet.CXFServlet();
        ServletRegistrationBean servletDef = new ServletRegistrationBean(cxfServlet, "/" + servicePath + "/*");
        servletDef.setLoadOnStartup(1);
        return servletDef;
    }

    public static void main(String[] args) {
        // create application
        SpringApplication app = new SpringApplication(Application.class);

        // add pid / port file writers
        app.addListeners(new ApplicationPidFileWriter());
        app.addListeners(new EmbeddedServerPortFileWriter());

        // run application
        app.run(args);
    }

}
