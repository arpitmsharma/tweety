package dev.cdc;

import java.util.HashMap;

import org.junit.Test;

import dev.cdc.service.ClusteringEngine;
import dev.cdc.service.mkmpp.KMeansOptions;

public class CdctweetyApplicationTests {

	@Test
	public void testCluster() {
		HashMap<String, String> textMap = new HashMap<String, String>(50);
		textMap.put("1", "love break apple apple love");
		textMap.put("2", "love apple orange orange");
		textMap.put("3", "guava orange break kiwi");
		textMap.put("4", "kiwi good awesome");
		textMap.put("5", "awesome fruit random");
		textMap.put("6", "fruit cool");
		textMap.put("7", "slurp pandemic eloquent");
		textMap.put("8", "whatever as always");
		textMap.put("9", "madness less more crore");
		textMap.put("10", "eloquent madness");
		KMeansOptions kmeansOptions = new KMeansOptions(textMap, 5);
		new ClusteringEngine().clusterKMeans(kmeansOptions);
	}

	public void testCluster1() {
		HashMap<String, String> textMap = new HashMap<String, String>(50);
		textMap.put("1", "love break apple apple love");
		textMap.put("2", "love apple orange orange");
		textMap.put("3", "guava orange break kiwi");
		textMap.put("4", "kiwi good awesome");
		textMap.put("5", "awesome fruit random");
		textMap.put("6", "fruit cool");
		textMap.put("7", "slurp pandemic eloquent");
		textMap.put("8", "eloquent madness");
		textMap.put("9", "madness less more crore");
		textMap.put("10", "whatever as always");
		KMeansOptions kmeansOptions = new KMeansOptions(textMap, 3);
		new ClusteringEngine().clusterKMeans(kmeansOptions);
	}

}
