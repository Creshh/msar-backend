package de.tuchemnitz.tomkr.msar.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tuchemnitz.tomkr.msar.search.ElasticService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticServiceTest {

	private static Logger LOG = LoggerFactory.getLogger(ElasticServiceTest.class);

	@Autowired
	ElasticService elastic;

	@Autowired
	JsonHelper json;
	

	@Before
	public void before() {
		elastic.deleteIndex();
	}

	
	
	@Test
	public void testSaveAndRetrieve() throws InterruptedException {
		LOG.debug("------------------------ testSaveAndRetrieve ------------------------ ");
		String basePath = "D:\\ws\\eclipse_ws\\metaapp2\\src\\test\\resources\\jsonExamples\\";
		
		Map<String, String> metaData = new HashMap<>();
		metaData.put("exifExample1.json", "exif");
		metaData.put("exifExample2.json", "exif");
		metaData.put("exifExample3.json", "exif");
		metaData.put("objectsExample1.json", "objects");
		metaData.put("objectsExample2.json", "objects");
		metaData.put("locationExample1.json", "location");
		metaData.put("locationExample2.json", "location");
		metaData.put("locationExample3.json", "location");
		
		for(Entry<String, String> entry : metaData.entrySet()) {
			Map<String, Object> map = JsonHelper.readJsonToMapFromFile(basePath + entry.getKey());
			elastic.indexDocument(map);
		}
		
		Thread.sleep(5000);
		LOG.debug("------------------------ indexing done ------------------------");
		
//		elastic.searchAll();
		
		elastic.searchByValue("Nikon");
		elastic.searchByValue("Canon");
		elastic.searchByValue("EF50mm + Nikon");
		elastic.searchByValue("EF50mm + -Nikon");
		
		elastic.searchByValue("chair");
		elastic.searchByValue("chair + table");
		elastic.searchByValue("type:objects + -table");
		
		elastic.getSuggestions("Dresde", "city.completion");
		elastic.getSuggestions("Dre", "city.completion");
		elastic.getSuggestions("D", "city.completion");
		
		elastic.getSuggestions("Der", "city.completion");
		elastic.getSuggestions("Dersden", "city.completion");
		elastic.getSuggestions("Dersdi", "city.completion");
		elastic.getSuggestions("B", "city.completion");
		
		
		elastic.getSuggestions("Cha", "objects.completion");
		elastic.getSuggestions("Ta", "objects.completion");
		
		LOG.debug("--------------------------------------------------------------------- ");
	}
}
