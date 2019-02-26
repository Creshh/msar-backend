package de.tuchemnitz.tomkr.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

import de.tuchemnitz.tomkr.meta.input.JSONAdapter;
import de.tuchemnitz.tomkr.meta.model.Asset;
import de.tuchemnitz.tomkr.meta.model.MetaObject;
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
	JSONAdapter jsonAdapter;

	@Autowired
	private ElasticsearchTemplate esTemplate;

	@Before
	public void before() {
		esTemplate.deleteIndex(Asset.class);
		esTemplate.createIndex(Asset.class);
		esTemplate.putMapping(Asset.class);
		esTemplate.refresh(Asset.class);
	}

	private List<Asset> create(){
		List<Asset> results = new ArrayList<Asset>();
		
		int MAX = 4;
		int P = 3;
		
		String[][][] paths = new String[P][MAX][];
		String[] values = {"v1", "v2", "v3", "v4"};
		
		paths[0][0] = new String[]{"p1", "p2.1", "p3", "p4"};
		paths[0][1] = new String[]{"p1"};
		paths[0][2] = new String[]{"p1", "p2.1"};
		paths[0][3] = new String[]{"p1", "p2.2"};
		
		paths[1][0] = new String[]{"p1", "p2.1", "p3.2", "p4.1"};
		paths[1][1] = new String[]{"p1"};
		paths[1][2] = new String[]{"p1", "p2.1"};
		paths[1][3] = new String[]{"p1", "p2.1", "p3.2", "p4.2"};
		
		paths[2][0] = new String[]{"p1"};
		paths[2][1] = new String[]{"p2"};
		paths[2][2] = new String[]{"p3"};
		paths[2][3] = new String[]{"p4"};
		

		for(int k = 0; k<3; k++) {
			Asset asset = new Asset("asset0" + (k+1));
			for(int i = 0; i< MAX; i++) {
				asset.addValue(paths[k][i], values[i]);
			}
			results.add(asset);
		}
		
		return results;
	}
	
	
	@Test
	public void testSaveFromJson() {
		String test1FilePath = "D:\\ws\\eclipse_ws\\metaapp\\src\\main\\java\\de\\tuchemnitz\\tomkr\\meta\\test1.json";
		MetaObject obj1 = jsonAdapter.readJSON(test1FilePath);
		
		String test2FilePath = "D:\\ws\\eclipse_ws\\metaapp\\src\\main\\java\\de\\tuchemnitz\\tomkr\\meta\\test2.json";
		MetaObject obj2 = jsonAdapter.readJSON(test2FilePath);
		
		
		Asset asset = new Asset("ref");
		asset.getMetaData().put("openpose", obj1);
		asset.getMetaData().put("detectron", obj2);
		
		metaService.save(asset);
		LOG.debug("done");
	}
	
//	@Test
	public void testSave() {
		LOG.debug("testSave");
		
		List<Asset> assets = create();
		
		
		Asset testAsset = metaService.save(assets.get(0));

//		assertNotNull(testAsset.getFileName());
//		assertEquals(testMetaFile.getFileName(), metaFile.getFileName());
//		assertEquals(testMetaFile.getLocation(), metaFile.getLocation());
	}

//	@Test
	public void testFindAll() {
		LOG.debug("testFindAll");
		
		List<Asset> assets = create();
		
		Iterable<Asset> testAssets = metaService.findAll();
		int testCount = 0;
		for(Asset testAsset : testAssets) {
			testCount++;
//			LOG.debug(String.format("%d: %s | %s", testCount, testMetaFile.getFileName(), testMetaFile.getLocation()));
//			assertTrue(testMetaFile.getFileName().startsWith(baseName.split("%")[0]));
//			assertTrue(testMetaFile.getLocation().startsWith(baseLocation.split("%")[0]));
		}
		
		assertEquals(assets.size(), testCount);
	}
	
	// TODO: how to parse json and dynamically insert into key values... try iterate through jsonobjects -> instanceof array or instanceof dict -> enter as value or new subnode

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
