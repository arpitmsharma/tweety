package dev.cdc.service.mkmpp;

import org.apache.commons.math3.ml.clustering.Clusterable;

public interface CustomClusterable extends Clusterable {

	/**
	 * Gets the n-dimensional point.
	 *
	 * @return the point array
	 */
	double[] getPoint();

	String getId();

	String getText();

}
