package de.tuchemnitz.tomkr.msar.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tuchemnitz.tomkr.msar.search.IndexService;
import de.tuchemnitz.tomkr.msar.search.TypeRegistry;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentHandlerTest {

	private static Logger LOG = LoggerFactory.getLogger(DocumentHandlerTest.class);

	@Autowired
	DocumentHandler documentHandler;
	
	@Autowired
	IndexService indexService;

	@Before
	public void before() {
		indexService.deleteIndex(TypeRegistry.INDEX);
	}

	
	
	
	@Test
	public void testAddDocument() {
		LOG.debug("testAddDocument");
		
		String file = "";
		
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
