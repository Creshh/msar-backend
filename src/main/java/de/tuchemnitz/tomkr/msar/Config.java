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

import de.tuchemnitz.tomkr.msar.core.registry.DataTypeMapper;

/**
 * 
 * @author Kretzschmar
 *
 */
@Configuration
//@EnableElasticsearchRepositories(basePackages = "de.tuchemnitz.tomkr.msar")
//@EnableMongoRepositories(basePackages = "de.tuchemnitz.tomkr.metaapp.service.mongo")
public class Config {

	private static Logger LOG = LoggerFactory.getLogger(Config.class);

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;

	@Value("${elasticsearch.clustername}")
	private String clusterName;

	@Value("${elasticsearch.index}")
	private String index;
	
	@Value("${elasticsearch.type}")
	private String type;
	
	@Value("${typeMapping.resource}")
	private String typeMappingRes;
	
	private Client client;

	@Bean
	public DataTypeMapper getDataTypeMapper() {
		return new DataTypeMapper(typeMappingRes);
	}
	
	@Bean
	public Client getClient() {
		if (client == null) {
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

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getClusterName() {
		return clusterName;
	}

	public String getTypeMappingRes() {
		return typeMappingRes;
	}

	public String getIndex() {
		return index;
	}

	public String getType() {
		return type;
	}
}
