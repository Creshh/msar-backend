package de.tuchemnitz.tomkr.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
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

import de.tuchemnitz.tomkr.meta.model.Asset;
import de.tuchemnitz.tomkr.meta.search.MetaRetrievalRepository;
import de.tuchemnitz.tomkr.meta.search.MetaRetrievalService;
import de.tuchemnitz.tomkr.metaapp.model.MetaFile;
import de.tuchemnitz.tomkr.metaapp.service.es.MetaElasticService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticMetaTest {

	private static Logger LOG = LoggerFactory.getLogger(ElasticMetaTest.class);

	@Autowired
	private MetaRetrievalService metaService;

	@Autowired
	private ElasticsearchTemplate esTemplate;

	@Before
	public void before() {
		esTemplate.deleteIndex(Asset.class);
		esTemplate.createIndex(Asset.class);
		esTemplate.putMapping(Asset.class);
		esTemplate.refresh(Asset.class);
	}

	@Test
	public void testSave() {
		LOG.debug("testSave");
		
		String[] path1 = {"p1", "p2.1", "p3", "p4"};
		String[] path2 = {"p1" };
		String[] path3 = {"p1", "p2.1"};
		String[] path4 = {"p1", "p2.2"};
		
		String value1 = "v1";
		String value2 = "v2";
		String value3 = "v3";
		String value4 = "v4";
		
		Asset asset = new Asset("asset01");
		asset.addValue(path1, value1);
		asset.addValue(path2, value2);
		asset.addValue(path3, value3);
		asset.addValue(path4, value4);
		
		
		Asset testAsset = metaService.save(asset);

//		assertNotNull(testAsset.getFileName());
//		assertEquals(testMetaFile.getFileName(), metaFile.getFileName());
//		assertEquals(testMetaFile.getLocation(), metaFile.getLocation());
	}

//	@Test
//	public void testFindAll() {
//		LOG.debug("testFindAll");
//		String baseName = "file%d";
//		String baseLocation = "location%d";
//		int fileCount = 10;
//		for (int i = 0; i < fileCount; i++) {
//			MetaFile metaFile = new MetaFile(String.format(baseName, i), String.format(baseLocation, i%2));
//			metaService.save(metaFile);
//		}
//		
//		Iterable<MetaFile> testMetaFiles = metaService.findAll();
//		int testCount = 0;
//		for(MetaFile testMetaFile : testMetaFiles) {
//			testCount++;
////			LOG.debug(String.format("%d: %s | %s", testCount, testMetaFile.getFileName(), testMetaFile.getLocation()));
//			assertTrue(testMetaFile.getFileName().startsWith(baseName.split("%")[0]));
//			assertTrue(testMetaFile.getLocation().startsWith(baseLocation.split("%")[0]));
//		}
//		assertEquals(fileCount, testCount);
//	}
//
//	@Test
//	public void testDelete() {
//		LOG.debug("testDelete");
//		String fileName = "file01";
//		MetaFile metaFile = new MetaFile(fileName, "location01");
//		metaService.save(metaFile);
//		metaService.delete(metaFile);
//		List<MetaFile> testMetaFiles = metaService.findByFileName(fileName);
//		assertTrue(testMetaFiles.isEmpty());
//	}
	
	
}
