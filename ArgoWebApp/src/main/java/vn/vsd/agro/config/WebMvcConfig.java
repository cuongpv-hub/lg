package vn.vsd.agro.config;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import vn.vsd.agro.validator.VietnamNumberEditor;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	/* Time, in seconds, to have the browser cache static resources (one week). */
    private static final int BROWSER_CACHE_CONTROL = 604800;
    private static final String DEFAULT_LOCALE = "vi";
    
    @Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
    
	@Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[] { "/WEB-INF/views/tiles.xml" });
        tilesConfigurer.setCheckRefresh(true);
         
        return tilesConfigurer;
    }
	
	/*@Bean(name = "viewResolver")
	public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
		ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
		contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");

		MappingJackson2JsonView defaultView = new MappingJackson2JsonView();
		defaultView.setExtractValueFromSingleKeyModel(true);

		ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
		contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
		contentViewResolver.setViewResolvers(Arrays.<ViewResolver> asList(viewResolver));
		contentViewResolver.setDefaultViews(Arrays.<View> asList(defaultView));
		return contentViewResolver;
	}*/
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
	    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	    resolver.setDefaultEncoding("utf-8");
	    return resolver;
	}
	
	@Bean
    public LocaleResolver localeResolver() {
        final CookieLocaleResolver ret = new CookieLocaleResolver();
        ret.setDefaultLocale(new Locale(DEFAULT_LOCALE));
        return ret;
    }

	@Bean(name = "numberPropertyEditor")
	public PropertyEditor numberPropertyEditor() {
		Locale viLocale = new Locale(DEFAULT_LOCALE);
		NumberFormat numberFormat = NumberFormat.getInstance(viLocale);
		
		return new VietnamNumberEditor(BigDecimal.class, numberFormat, true);
	}
	
	@Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource ret = new ReloadableResourceBundleMessageSource();
        ret.setBasename("classpath:language");
        ret.setDefaultEncoding("UTF-8");
        return ret;
    }
	
	@Bean 
	public LocaleChangeInterceptor localeChangeInterceptor(){
	    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName("lang");
	    return localeChangeInterceptor;
	}
	
	/**
     * Configure ViewResolvers to deliver views.
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        TilesViewResolver viewResolver = new TilesViewResolver();
        registry.viewResolver(viewResolver);
    }
    
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/resources/**")
            .addResourceLocations("/resources/")
            .setCachePeriod(BROWSER_CACHE_CONTROL);
	    
	    registry.addResourceHandler("/upload/**")
        	.addResourceLocations("/WEB-INF/upload/")
        	.setCachePeriod(BROWSER_CACHE_CONTROL);
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/mobile/**")
        	.allowedOrigins("*")
        	.allowedMethods("*")
        	.allowedHeaders("*")
        	.allowCredentials(true);
    }
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
	}
}