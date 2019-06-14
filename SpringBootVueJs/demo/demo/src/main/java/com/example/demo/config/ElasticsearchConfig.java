package com.example.demo.config;

import org.apache.http.HttpHost;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
	/*
	@Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;
	*/
    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
    	/*
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		RestHighLevelClient client = new RestHighLevelClient(builder);
		*/
    	RestHighLevelClient client = new RestHighLevelClient(
    	        RestClient.builder(
    	                new HttpHost("localhost", 9200, "http"),
    	                new HttpHost("localhost", 9201, "http")));
        
        return client;

    }

}
