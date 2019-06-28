package de.tuchemnitz.tomkr.msar.utils;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDataGeneratorTest {
	private static Logger LOG = LoggerFactory.getLogger(HelpersTest.class);

	@Autowired
	TestDataGenerator testDataGenerator;
	
	
	@Before
	public void before() {
		
	}

	@Test
	public void cleanIndex() {
		testDataGenerator.cleanIndex();
	}
	
	
//	@Test
	public void generateLocalData() throws JsonGenerationException, JsonMappingException, IOException {
		
		testDataGenerator.generateJsonFiles();
	}
}
