package br.gov.pa.igeprev.siaag.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WelcomePageRedirect extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/pages/logout")
                .setViewName("forward:/logout");
        registry.addViewController("siaag/pages/logout")
                .setViewName("forward:/logout");
        registry.addViewController("/alterar-senha")
                .setViewName("forward:/pages/protected/alterar-senha.xhtml");
        registry.addViewController("/recuperar-senha")
                .setViewName("forward:/pages/public/recuperar-senha.xhtml");
        registry.addViewController("/login")
                .setViewName("forward:/pages/login.xhtml");
        registry.addViewController("siaag/login")
                .setViewName("forward:/pages/login.xhtml");
        /*registry.addViewController("/")
                .setViewName("forward:/pages/protected/home.xhtml");*/
        registry.addViewController("")
                .setViewName("forward:/pages/protected/home.xhtml");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

        super.addViewControllers(registry);
    }
}