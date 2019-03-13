package de.tuchemnitz.tomkr.msar.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tuchemnitz.tomkr.msar.utils.Helpers;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HelpersTest {

	private static Logger LOG = LoggerFactory.getLogger(HelpersTest.class);


	@Before
	public void before() {
	}

	
	
	@Test
	public void testLoadProperties() {
		LOG.debug("testLoadProperties");
		
		Properties prop = Helpers.loadProperties("typeMapping.properties");
		
		assertNotNull(prop);
		assertFalse(prop.isEmpty());
	}
	
}
