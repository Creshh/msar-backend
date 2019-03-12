package de.tuchemnitz.tomkr.msar.reader;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import de.tuchemnitz.tomkr.msar.core.DocumentHandler;
import de.tuchemnitz.tomkr.msar.core.registry.SchemaHandler;
import de.tuchemnitz.tomkr.msar.core.registry.TypeRegistry;
import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentHandlerTest {

	private static Logger LOG = LoggerFactory.getLogger(DocumentHandlerTest.class);

	@Autowired
	DocumentHandler documentHandler;
	
	@Autowired
	IndexFunctions indexService;
	
	@Autowired
	SchemaHandler schemaHandler;

	@Before
	public void before() throws FileNotFoundException {
		try {
			indexService.deleteIndex(TypeRegistry.INDEX);
		} catch (Exception e) {
			LOG.error("Error while deleting index", e);
		}
		
		File file = ResourceUtils.getFile(String.format("classpath:%s", "schemaExamples/exif.json"));
		InputStream is = null;
		String json = null;
		try {
			is = new FileInputStream(file);
			json = IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOG.error("Error reading from InputStream", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("Error closing InputStream", e);
				}
			}
		}
		
		schemaHandler.registerSchema("exif", json);
	}

	
	
	
	@Test
	public void testAddDocument() {
		LOG.debug("testAddDocument");
		
		String file = "D:\\ws\\eclipse_ws\\metaapp2\\src\\test\\resources\\jsonExamples\\exifExample1.json";
		
		InputStream is = null;
		String json = null;
		try {
			is = new FileInputStream(file);
			json = IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOG.error("Error reading from InputStream", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("Error closing InputStream", e);
				}
			}
		}
		boolean result = documentHandler.addDocument(json);
		
		assertTrue(result);
	}
}
