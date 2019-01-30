package de.tuchemnitz.tomkr.metaapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import de.tuchemnitz.tomkr.metaapp.es.service.MetaService;
import de.tuchemnitz.tomkr.metaapp.model.MetaFile;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticMetaServiceTest {

	private static Logger LOG = LoggerFactory.getLogger(ElasticMetaServiceTest.class);

	@Autowired
	private MetaService metaService;

	@Autowired
	private ElasticsearchTemplate esTemplate;

	@Before
	public void before() {
		esTemplate.deleteIndex(MetaFile.class);
		esTemplate.createIndex(MetaFile.class);
		esTemplate.putMapping(MetaFile.class);
		esTemplate.refresh(MetaFile.class);
	}

	@Test
	public void testSave() {
		LOG.debug("testSave");
		MetaFile metaFile = new MetaFile("file01", "location01");
		MetaFile testMetaFile = metaService.save(metaFile);

		assertNotNull(testMetaFile.getFileName());
		assertEquals(testMetaFile.getFileName(), metaFile.getFileName());
		assertEquals(testMetaFile.getLocation(), metaFile.getLocation());
	}

	@Test
	public void testFindByName() {
		LOG.debug("testFindByName");
		String fileName = "file01";
		MetaFile metaFile = new MetaFile(fileName, "location01");
		metaService.save(metaFile);

		List<MetaFile> testMetaFiles = metaService.findByFileName(fileName);
		assertEquals(1, testMetaFiles.size());
		assertNotNull(testMetaFiles.get(0));
		assertEquals(testMetaFiles.get(0).getFileName(), metaFile.getFileName());
		assertEquals(testMetaFiles.get(0).getLocation(), metaFile.getLocation());
	}

	@Test
	public void testFindByLocation() {
		LOG.debug("testFindByLocation");
		String baseName = "file%d";
		String baseLocation = "location%d";
		int[] fileCount = new int[]{4, 6, 10};
		int start = 0;
		for(int k = 0; k < fileCount.length; k++) {
			for (int i = start; i < fileCount[k]+start; i++) {
				MetaFile metaFile = new MetaFile(String.format(baseName, i), String.format(baseLocation, k));
				metaService.save(metaFile);
			}
			start += fileCount[k];
		}
		
		for(int j = 0; j< fileCount.length; j++) {
			Page<MetaFile> testMetaFiles1 = metaService.findByLocation(String.format(baseLocation, j), PageRequest.of(0, 10));
			assertEquals(fileCount[j], testMetaFiles1.getTotalElements());
		}
	}
	
	@Test
	public void testFindByLocationFuzzy() {
		LOG.debug("testFindByLocationFuzzy");
		String baseName = "file%d";
		String baseLocation = "location%d";
		int[] fileCount = new int[]{4, 6, 10};
		int start = 0;
		for(int k = 0; k < fileCount.length; k++) {
			for (int i = start; i < fileCount[k]+start; i++) {
				MetaFile metaFile = new MetaFile(String.format(baseName, i), String.format(baseLocation, k));
				metaService.save(metaFile);
			}
			start += fileCount[k];
		}
		
		for(int j = 0; j< fileCount.length; j++) {
			Page<MetaFile> testMetaFiles1 = metaService.findByLocationFuzzy(String.format("location%d", j),"0", PageRequest.of(0, 10));
			assertEquals(fileCount[j], testMetaFiles1.getTotalElements());
		}
		
		for(int j = 0; j< fileCount.length; j++) {
			Page<MetaFile> testMetaFiles2 = metaService.findByLocationFuzzy(String.format("lication%d", j),"1", PageRequest.of(0, 10));
			assertEquals(fileCount[j], testMetaFiles2.getTotalElements());
		}
		
		for(int j = 0; j< fileCount.length; j++) {
			Page<MetaFile> testMetaFiles3 = metaService.findByLocationFuzzy(String.format("loctaion%d", j),"AUTO", PageRequest.of(0, 10));
			assertEquals(fileCount[j], testMetaFiles3.getTotalElements());
		}
	}
	
	@Test
	public void testFindAll() {
		LOG.debug("testFindAll");
		String baseName = "file%d";
		String baseLocation = "location%d";
		int fileCount = 10;
		for (int i = 0; i < fileCount; i++) {
			MetaFile metaFile = new MetaFile(String.format(baseName, i), String.format(baseLocation, i%2));
			metaService.save(metaFile);
		}
		
		Iterable<MetaFile> testMetaFiles = metaService.findAll();
		int testCount = 0;
		for(MetaFile testMetaFile : testMetaFiles) {
			testCount++;
//			LOG.debug(String.format("%d: %s | %s", testCount, testMetaFile.getFileName(), testMetaFile.getLocation()));
			assertTrue(testMetaFile.getFileName().startsWith(baseName.split("%")[0]));
			assertTrue(testMetaFile.getLocation().startsWith(baseLocation.split("%")[0]));
		}
		assertEquals(fileCount, testCount);
	}

	@Test
	public void testDelete() {
		LOG.debug("testDelete");
		String fileName = "file01";
		MetaFile metaFile = new MetaFile(fileName, "location01");
		metaService.save(metaFile);
		metaService.delete(metaFile);
		List<MetaFile> testMetaFiles = metaService.findByFileName(fileName);
		assertTrue(testMetaFiles.isEmpty());
	}
	
	
}
