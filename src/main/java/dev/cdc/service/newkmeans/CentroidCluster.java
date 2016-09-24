package dev.cdc.service.newkmeans;

public class CentroidCluster<T extends Clusterable> extends Cluster<T> {

	/** Serializable version identifier. */
	private static final long serialVersionUID = -3075288519071812288L;

	/** Center of the cluster. */
	private final Clusterable center;

	/**
	 * Build a cluster centered at a specified point.
	 *
	 * @param center
	 *            the point which is to be the center of this cluster
	 */
	public CentroidCluster(final Clusterable center) {
		super();
		this.center = center;
	}

	/**
	 * Get the point chosen to be the center of this cluster.
	 *
	 * @return chosen cluster center
	 */
	public Clusterable getCenter() {
		return center;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Cluster\n");
		for (T point : this.getPoints()) {
			sb.append("\t"+point.toString()+"\n");
		}
		return sb.toString();
	}

}