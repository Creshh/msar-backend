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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.tuchemnitz.tomkr.msar.core.registry.DataTypeMapper;

/**
 * Configuration class, holding all configurable information and global instances of utility classes.
 * 
 * @author Tom Kretzschmar
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = "de.tuchemnitz.tomkr.msar.db")
public class Config {

	private static Logger LOG = LoggerFactory.getLogger(Config.class);

	/**
	 * Hostname of elasticsearch instance.
	 */
	@Value("${elasticsearch.host}")
	private String host;

	/**
	 * Port of elasticsearch instance.
	 */
	@Value("${elasticsearch.port}")
	private int port;

	/**
	 * Name of elasticsearch instance.
	 */
	@Value("${elasticsearch.clustername}")
	private String clusterName;

	/**
	 * Name of the used index in elasticsearch.
	 */
	@Value("${elasticsearch.index}")
	private String index;
	
	/**
	 * Name of the used type in elasticsearch.
	 */
	@Value("${elasticsearch.type}")
	private String type;
	
	/**
	 * Filename of the typemapping resource file.
	 */
	@Value("${typeMapping.resource}")
	private String typeMappingRes;
	
	/**
	 * Filename of the meta-schema-file from json-schema.
	 */
	@Value("${metaSchema.resource}")
	private String metaSchemaRes;
	
	/**
	 * enables fuzzyness in suggestions (autocorrect typing errors)
	 */
	@Value("${suggestion.fuzzy}")
	private boolean fuzzySuggestion;
	
	/**
	 * root folder for asset / image storage; must be accessible by the application
	 */
	@Value("${storage.base}")
	private String storageBase;
	
	/**
	 * batch size of elasticsearch requests 
	 */
	@Value("${elasticsearch.scrollsize}")
	private int scrollSize;
	
	/**
	 * Elasticsearch client interface
	 * {@link Client}
	 */
	private Client client;

	

	/**
	 * Get DataTypeMapper instance.
	 * @return {@link DataTypeMapper}
	 */
	@Bean
	public DataTypeMapper getDataTypeMapper() {
		return new DataTypeMapper(typeMappingRes);
	}
	
	/**
	 * Get elasticsearch client interface or create one if no instance is available.
	 * @return The client instance
	 */
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

	/**
	 * Getter.
	 * @return The elasticsearch hostname.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Getter.
	 * @return The elasticsearch port number.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Getter.
	 * @return The elasticsearch cluster name.
	 */
	public String getClusterName() {
		return clusterName;
	}

	/**
	 * Getter.
	 * @return The type mapping resource file name.
	 */
	public String getTypeMappingRes() {
		return typeMappingRes;
	}

	/**
	 * Getter.
	 * @return The elasticsearch index name.
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * Getter.
	 * @return The elasticsearch mapping type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Getter.
	 * @return The meta schema resource file name.
	 */
	public String getMetaSchemaRes() {
		return metaSchemaRes;
	}

	/**
	 * Getter.
	 * @return The fuzzyness flag.
	 */
	public boolean getFuzzySuggestion() {
		return fuzzySuggestion;
	}
	
	/**
	 * Getter.
	 * @return The storage root path.
	 */
	public String getStorageBase() {
		return storageBase;
	}

	/**
	 * Getter.
	 * @return The elasticsearch query batch size.
	 */
	public int getScrollSize() {
		return scrollSize;
	}
}
