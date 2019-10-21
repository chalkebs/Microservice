package com.fundoo.elasticSearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("rawtypes")
@Configuration
public class ElasticSearchConfig extends AbstractFactoryBean
{    
    @Value("${elasticsearch.host}")
    private String host;
    
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void destroy() 
    {
        try 
        {
            if (restHighLevelClient != null) 
            {
                restHighLevelClient.close();
            }
        } 
        catch (final Exception e)
        {
        	e.printStackTrace();
        }
        
    }

    @Override
    public Class<RestHighLevelClient> getObjectType() 
    {
        return RestHighLevelClient.class;
    }

    @Override
    public boolean isSingleton() 
    {
        return false;
    }

    @Override
    public RestHighLevelClient createInstance() 
    {
        return buildClient();
    }

    private RestHighLevelClient buildClient() 
    {
        try 
        {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(host)));
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        
        return restHighLevelClient;
        
    }
}

