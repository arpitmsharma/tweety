package dev.cdc.service.mkmpp;

import java.util.HashMap;

public class KMeansOptions {
	private HashMap<String, String> textMap = new HashMap<String, String>();
	private int nClusters;

	public KMeansOptions() {
		super();
	}

	public KMeansOptions(HashMap<String, String> textMap, int nClusters) {
		super();
		this.textMap = textMap;
		this.nClusters = nClusters;
	}

	public HashMap<String, String> getTextMap() {
		return textMap;
	}

	public void setTextMap(HashMap<String, String> textMap) {
		this.textMap = textMap;
	}

	public int getnClusters() {
		return nClusters;
	}

	public void setnClusters(int nClusters) {
		this.nClusters = nClusters;
	}

}
