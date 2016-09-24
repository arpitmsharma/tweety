package dev.cdc.service.mkmpp;

public interface Clusterable extends org.apache.commons.math3.ml.clustering.Clusterable {

	/**
	 * Gets the n-dimensional point.
	 *
	 * @return the point array
	 */
	double[] getPoint();

	String getId();

	String getText();

}
