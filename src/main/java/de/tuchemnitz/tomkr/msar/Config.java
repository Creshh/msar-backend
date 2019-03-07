package de.tuchemnitz.tomkr.msar;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableElasticsearchRepositories(basePackages = "de.tuchemnitz.tomkr.msar")
//@EnableMongoRepositories(basePackages = "de.tuchemnitz.tomkr.metaapp.service.mongo")
public class Config {
	
	private static Logger LOG = LoggerFactory.getLogger(Config.class);


	@Value("${elasticsearch.host}")
	public String host;
	
	@Value("${elasticsearch.port}")
	public int port;
	
	@Value("${elasticsearch.clustername}")
	public String clusterName;
	
	private Client client;
	
	@Bean
	public Client getClient() {
		if(client == null) {
			Settings settings = Settings.builder().put("cluster.name", clusterName).build();
			TransportClient client = new PreBuiltTransportClient(settings);
			try {
				client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
			} catch (UnknownHostException e) {
				LOG.error("Error while retrieving host " + host, e);
			}
			this.client = client;
		}
		return this.client;
	}
}
