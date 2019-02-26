package de.tuchemnitz.tomkr.meta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tuchemnitz.tomkr.meta.input.JSONAdapter;
import de.tuchemnitz.tomkr.meta.model.MetaObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JSONAdapterTest {

	private static Logger LOG = LoggerFactory.getLogger(JSONAdapterTest.class);
	
	@Autowired
	JSONAdapter jsonAdapter;
	
	@Test
	public void testParse() {
		String test1FilePath = "D:\\ws\\eclipse_ws\\metaapp\\src\\main\\java\\de\\tuchemnitz\\tomkr\\meta\\test1.json";
		MetaObject obj1 = jsonAdapter.readJSON(test1FilePath);
		
		String test2FilePath = "D:\\ws\\eclipse_ws\\metaapp\\src\\main\\java\\de\\tuchemnitz\\tomkr\\meta\\test2.json";
		MetaObject obj2 = jsonAdapter.readJSON(test2FilePath);
		
		
		
		LOG.debug("done");
	}

}
