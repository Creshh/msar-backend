package de.tuchemnitz.tomkr.msar.reader;

import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonHelperTest {

	private static Logger LOG = LoggerFactory.getLogger(JsonHelperTest.class);


	@Before
	public void before() {
	}

	
	
//	@Test
	public void testValidate() {
		LOG.debug("testValidate");
//		jsonHelper.validateNew();
	}
	
	@Test
	public void testReadFromResource() {
		LOG.debug("testValidate");
		JSONObject json = JsonHelper.loadJSONFromResource("meta-schema-v7.json");
		
		assertNotNull(json);
	}
}
