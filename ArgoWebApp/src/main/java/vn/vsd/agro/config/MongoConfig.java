package vn.vsd.agro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;

import vn.vsd.agro.context.DbContext;
import vn.vsd.agro.context.MongoContext;
import vn.vsd.agro.domain.PO;

@Configuration
@ComponentScan(basePackages = { 
		"vn.vsd.agro.domain", 
		"vn.vsd.agro.repository" 
})
public class MongoConfig extends AbstractMongoConfiguration
{
	public MongoConfig()
	{
		super();
	}
	
	@Value("${mongo.host}")
	private String host;
	
	@Value("${mongo.port}")
	private int port;
	
	@Value("${mongo.databasename}")
	private String dbName;
	
	@Value("${mongo.username}")
	private String username;
	
	@Value("${mongo.password}")
	private String password;
	
	@Value("${mongo.connection.per.host:100}")
	private int connectionPerHost;
	
	@Value("${mongo.max.waiting.thead:1500}")
	private int maxWaitingThread;
	
    public MongoClientFactoryBean mongoFactoryBean() throws Exception
    {
	    MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
	    builder.connectionsPerHost(connectionPerHost);
	    builder.threadsAllowedToBlockForConnectionMultiplier(maxWaitingThread);
	    
	    MongoClientFactoryBean mongoFactoryBean = new MongoClientFactoryBean();
		mongoFactoryBean.setHost(host);
		mongoFactoryBean.setPort(port);
		mongoFactoryBean.setMongoClientOptions(builder.build());
		mongoFactoryBean.afterPropertiesSet();
		return mongoFactoryBean;
	}
	
	@Override
	protected UserCredentials getUserCredentials() 
	{
		UserCredentials userCredentials = null;
		
		if (!StringUtils.isEmpty(username)
				&& !StringUtils.isEmpty(password))
		{
			userCredentials = new UserCredentials(username, password);
		}
		
		return userCredentials;
	}
	
	@Override
	protected String getDatabaseName() {
		return dbName;
	}

	@Bean
	@Override
	public Mongo mongo() throws Exception {
		return mongoFactoryBean().getObject();
	}
	
	@Override
	protected String getMappingBasePackage() {
		String domainPackageName = ClassUtils.getPackageName(PO.class);
	    return domainPackageName;
	}
	
	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}
	
	@Bean
	public DbContext dbContext()
	{
		return new MongoContext();
	}
}
