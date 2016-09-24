package dev.cdc.service.mkmpp;

import java.util.List;

public class ClusterWrapper {

	private String name;

	private List<KMeansPoint> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<KMeansPoint> getChildren() {
		return children;
	}

	public void setChildren(List<KMeansPoint> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "ClusterWrapper [name=" + name + ", children=" + children + "]";
	}

}
