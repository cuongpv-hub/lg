package vn.vsd.agro.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Value("${url.webview}")
    private String defaultRedirectUrl;
    
    @Value("${cors.allowed.origins}")
	private String allowedOrigins;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(userAuthenticationProvider());
    	auth.inMemoryAuthentication()
        	.withUser("user").password("password").roles("USER")
        	.and()
        	.withUser("admin").password("password").roles("USER", "ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
        		/*"/images/**", 
        		"/assets/**",
        		"/api/image/**",
        		"/api/notoken/**",
        		"/api/service/**",
        		"/api/test/**",
        		"/websocket/**",
        		"/ws/**",*/
        		"/**",
        		"/home",
        		"/test/**"
    		);
        
        // Ignore check access token with any request start with /api and have "ticket" parameter
        //web.ignoring().requestMatchers(new SignedRequestMatcher("/api/**"));
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// @formatter:off
    	http
        	.authorizeRequests().antMatchers("/*", "/home*", "/account/login", "/account/ping").permitAll()
	        .and()
	        	.authorizeRequests().anyRequest().hasRole("USER")
	        .and()
	        	.exceptionHandling()
	            	.accessDeniedPage("/account/login?authorization_error=true")
	            	//.authenticationEntryPoint(new LoginAuthenticationEntryPoint("/account/login"))
	        // XXX: put CSRF protection back into this endpoint
	        .and()
	        //	.csrf()
	        //    	.requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
	        .logout()
	            .logoutSuccessUrl("/account/login")
	            .logoutUrl("/account/logout")
	            .deleteCookies("JSESSIONID", CookieLocaleResolver.DEFAULT_COOKIE_NAME)
	            //.logoutSuccessHandler(new LogoutSuccessHandler())
	            .permitAll()
	        .and()
	        	.formLogin()
	        		.usernameParameter("j_username")
	        		.passwordParameter("j_password")
	        		.failureUrl("/account/login?authentication_error=true")
	        		.loginPage("/account/login")
	        		.loginProcessingUrl("/account/login")
	        		.defaultSuccessUrl(defaultRedirectUrl)
	        		//.successHandler(authenticationSuccessHandler())
	        ;
        
        // If no allowed origin, mean only allow from same domain
        if (!StringUtils.isEmpty(allowedOrigins)) {
        	http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(allowFromStrategy()));
        } else {
        	http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN));
        }
        // @formatter:on
    }
    
    /*@Bean
    public UserAuthenticationProvider userAuthenticationProvider() {
    	//return new UserAuthenticationProvider();
    	return new MultitenantUserAuthenticationProvider();
    }*/
    
    /*@Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
    	return new LoginSuccessHandler();
    	//return new MultitenantLoginSuccessHandler();
    }*/
    
    @Bean
    public AllowFromStrategy allowFromStrategy() {
    	List<String> listAllowedOrgins = Arrays.asList(allowedOrigins.split(","));
		return new AllowFromStrategyImpl(listAllowedOrgins);
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
    	return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
    
	private class AllowFromStrategyImpl implements AllowFromStrategy {
    	
    	private final Logger logger = LoggerFactory.getLogger(this.getClass());

		private Collection<String> allowedOrigins = Collections.emptyList();

		public AllowFromStrategyImpl(Collection<String> alloweds) {
			super();
			
			if (!CollectionUtils.isEmpty(alloweds)) {
				this.allowedOrigins = new ArrayList<String>(alloweds.size());
				for (String origin : alloweds) {
					this.allowedOrigins.add(getOrigin(origin));
				}
			}
		}

		protected boolean isAllowed(String origin) {
			if (StringUtils.isEmpty(origin)) {
				return false;
			}
			return allowedOrigins.contains(origin);
		}

		@Override
		public String getAllowFromValue(HttpServletRequest request) {
			String origin = request.getHeader("Origin");
			if (isAllowed(origin)) {
				return origin;
			}
			return null;
		}

		private String getOrigin(String url) {
			if (!url.startsWith("http:") && !url.startsWith("https:")) {
				return url;
			}

			String origin = null;

			try {
				URI uri = new URI(url);
				origin = uri.getScheme() + "://" + uri.getAuthority();
			} catch (URISyntaxException e) {
				logger.error("Cannot parse URI from " + url, e);
			}

			return origin;
		}
	}
	
	/*
	private class BasicRequestMatcher implements RequestMatcher
	{
		private List<String> patterns;
		
		public BasicRequestMatcher(String...patterns)
		{
			this.patterns = new ArrayList<String>();
			
			if (patterns != null)
			{
				for (String pattern : patterns)
				{
					if (pattern.trim().length() > 0)
					{
						this.patterns.add(pattern);
					}
				}
			}
		}
		
	    @Override
	    public boolean matches(HttpServletRequest request)
	    {
	        String auth = request.getHeader("Authorization");
	        boolean matched = (auth != null && auth.startsWith("Basic"));
	        
	        if (!matched || this.patterns == null || this.patterns.isEmpty())
			{
	        	return matched;
			}
	        
	        String context = request.getContextPath();
			String path = request.getRequestURI();
			if (!context.equals("/") && path.startsWith(context)) {
				path = path.substring(context.length());
			}
			
			AntPathMatcher matcher = new AntPathMatcher();
		    for (String pattern : this.patterns)
		    {
		        if (matcher.isPattern(pattern))
		        {
		            if (matcher.match(pattern, path))
		            {
		            	return true;
		            }
		        }
		        else
		        {
		        	//exact match
		            if (pattern.equalsIgnoreCase(path))
		            {
		            	return true;
		            }
		        }
		    }
		    
		    return false;
	    }
	}*/
}
