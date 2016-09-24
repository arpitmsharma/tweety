package dev.cdc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.springframework.stereotype.Service;

import dev.cdc.service.mkmpp.ClusterWrapper;
import dev.cdc.service.mkmpp.KMeansOptions;
import dev.cdc.service.mkmpp.KMeansPoint;
import dev.cdc.service.text.TextEngine;
import dev.cdc.service.text.TfIdfCalculator;

@Service
public class ClusteringEngine {

	private HashMap<String, List<String>> docMap = new HashMap<String, List<String>>();

	public List<ClusterWrapper> clusterKMeans(KMeansOptions co) {
		for (Entry<String, String> entry : co.getTextMap().entrySet()) {
			docMap.put(entry.getKey(), TextEngine.preprocessText(entry.getValue()));
		}
		int size = docMap.size();
		List<String> bag = new ArrayList<>();
		int k = 0;
		List<List<String>> documents = new ArrayList<List<String>>(docMap.values());
		for (List<String> list : documents) {
			for (String string : list) {
				if (!bag.contains(string))
					bag.add(string);
			}
		}
		double tfidfMap[][] = new double[size][bag.size()];
		double maxCoordinate = 0;
		double tfidf = 0;
		for (int i = 0; i < size; i++) {
			// for (Entry<String, List<String>> entry : docMap.entrySet()) {
			for (int j = 0; j < bag.size(); j++) {
				tfidf = TfIdfCalculator.tfIdf(documents.get(i), documents, bag.get(j)) * 10;
				tfidfMap[i][j] = tfidf;
				if (tfidf > maxCoordinate)
					maxCoordinate = tfidf;
			}
		}
		Collection<KMeansPoint> points = new ArrayList<>(tfidfMap.length);
		int dimensions = tfidfMap[0].length;
		int j = 0;
		for (Entry<String, String> entry : co.getTextMap().entrySet()) {
			points.add(new KMeansPoint(entry.getKey(), entry.getValue(), tfidfMap[j++]));
		}

		KMeansPlusPlusClusterer<KMeansPoint> kmpp = new KMeansPlusPlusClusterer<>(co.getnClusters());

		MultiKMeansPlusPlusClusterer<KMeansPoint> mkmpp = new MultiKMeansPlusPlusClusterer<KMeansPoint>(kmpp, 100);
		int i = 1;
		List<ClusterWrapper> cwList = new ArrayList<ClusterWrapper>();
		for (CentroidCluster<KMeansPoint> kmp : mkmpp.cluster(points)) {
			ClusterWrapper cw = new ClusterWrapper();
			cw.setName("" + i++);
			List<KMeansPoint> pList = new ArrayList<KMeansPoint>(kmp.getPoints().size());
			for (KMeansPoint p : kmp.getPoints()) {
				pList.add(p);
			}
			cw.setChildren(pList);
			cwList.add(cw);
			System.out.println(cw);
		}
		return cwList;
	}
}
