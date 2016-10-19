package io.spring.isomorphic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;
import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class IsomorphicApplication {


    public static void main(String[] args) {
        SpringApplication.run(IsomorphicApplication.class, args);
    }

    @Bean
    CommandLineRunner init(CommentRepository cr) {
        return args -> {

            cr.save(new Comment("Brian Clozel", "This is a test!"));
            cr.save(new Comment("Stéphane Nicoll", "This is a test too!"));

            System.out.println("---------------------------------");
            cr.findAll().forEach(System.out::println);
            System.out.println("---------------------------------");
        };
    }

    @Bean
    public ViewResolver reactViewResolver() {
        ScriptTemplateViewResolver viewResolver = new ScriptTemplateViewResolver();
        viewResolver.setPrefix("static/templates/");
        viewResolver.setSuffix(".ejs");
        return viewResolver;
    }

    @Bean
    public ScriptTemplateConfigurer reactConfigurer() {
        ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
        configurer.setEngineName("nashorn");
        configurer.setScripts("static/polyfill.js",
                "static/lib/js/ejs.min.js",
                "/META-INF/resources/webjars/react/15.3.1/react.js",
                "/META-INF/resources/webjars/react/15.3.1/react-dom-server.js",
//                "/META-INF/resources/webjars/react/0.13.1/JSXTransformer.js",

                "static/render.js",

                "static/assets/comment.js",
                "static/assets/comment-form.js",
                "static/assets/comment-list.js");
        configurer.setRenderFunction("render");
        configurer.setSharedEngine(false);
        return configurer;
    }
}
