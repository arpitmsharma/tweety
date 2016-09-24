package dev.cdc.service.newkmeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClusterer<T extends Clusterable> {

	/** The number of clusters. */
	private final int k;

	/** The maximum number of iterations. */
	private final int maxIterations;

	/** Random generator for choosing initial centers. */
	private final Random random = new Random();

	/**
	 * Build a clusterer.
	 * <p>
	 * The default strategy for handling empty clusters that may appear during
	 * algorithm iterations is to split the cluster with largest distance
	 * variance.
	 * <p>
	 * The euclidean distance will be used as default distance measure.
	 *
	 * @param k
	 *            the number of clusters to split the data into
	 */
	public KMeansPlusPlusClusterer(final int k) {
		this(k, -1);
	}

	/**
	 * Build a clusterer.
	 * <p>
	 * The default strategy for handling empty clusters that may appear during
	 * algorithm iterations is to split the cluster with largest distance
	 * variance.
	 *
	 * @param k
	 *            the number of clusters to split the data into
	 * @param maxIterations
	 *            the maximum number of iterations to run the algorithm for. If
	 *            negative, no maximum will be used.
	 * @param measure
	 *            the distance measure to use
	 * @param random
	 *            random generator to use for choosing initial centers
	 */
	public KMeansPlusPlusClusterer(final int k, final int maxIterations) {
		this.k = k;
		this.maxIterations = maxIterations;
	}

	/**
	 * Return the number of clusters this instance will use.
	 *
	 * @return the number of clusters
	 */
	public int getK() {
		return k;
	}

	/**
	 * Returns the maximum number of iterations this instance will use.
	 *
	 * @return the maximum number of iterations, or -1 if no maximum is set
	 */
	public int getMaxIterations() {
		return maxIterations;
	}

	/**
	 * Returns the random generator this instance will use.
	 *
	 * @return the random generator
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Runs the K-means++ clustering algorithm.
	 *
	 * @param points
	 *            the points to cluster
	 * @return a list of clusters containing the points
	 * @throws MathIllegalArgumentException
	 *             if the data points are null or the number of clusters is
	 *             larger than the number of data points
	 * @throws ConvergenceException
	 *             if an empty cluster is encountered and the
	 *             {@link #emptyStrategy} is set to {@code ERROR}
	 */
	public List<CentroidCluster<T>> cluster(final Collection<T> points) {
		// create the initial clusters
		List<CentroidCluster<T>> clusters = chooseInitialCenters(points);

		// create an array containing the latest assignment of a point to a
		// cluster
		// no need to initialize the array, as it will be filled with the first
		// assignment
		int[] assignments = new int[points.size()];
		assignPointsToClusters(clusters, points, assignments);

		// iterate through updating the centers until we're done
		final int max = (maxIterations < 0) ? Integer.MAX_VALUE : maxIterations;
		for (int count = 0; count < max; count++) {
			boolean emptyCluster = false;
			List<CentroidCluster<T>> newClusters = new ArrayList<CentroidCluster<T>>();
			for (final CentroidCluster<T> cluster : clusters) {
				Clusterable newCenter = null;
				if (cluster.getPoints().isEmpty()) {
					newCenter = getFarthestPoint(clusters);
					emptyCluster = true;
				} else {
					newCenter = centroidOf(cluster.getPoints(), cluster.getCenter().getPoint().length);
				}
				newClusters.add(new CentroidCluster<T>(newCenter));
			}
			int changes = assignPointsToClusters(newClusters, points, assignments);
			clusters = newClusters;

			// if there were no more changes in the point-to-cluster assignment
			// and there are no empty clusters left, return the current clusters
			if (changes == 0 && !emptyCluster) {
				return clusters;
			}
		}
		return clusters;
	}

	/**
	 * Adds the given points to the closest {@link Cluster}.
	 *
	 * @param clusters
	 *            the {@link Cluster}s to add the points to
	 * @param points
	 *            the points to add to the given {@link Cluster}s
	 * @param assignments
	 *            points assignments to clusters
	 * @return the number of points assigned to different clusters as the
	 *         iteration before
	 */
	private int assignPointsToClusters(final List<CentroidCluster<T>> clusters, final Collection<T> points,
			final int[] assignments) {
		int assignedDifferently = 0;
		int pointIndex = 0;
		for (final T p : points) {
			int clusterIndex = getNearestCluster(clusters, p);
			if (clusterIndex != assignments[pointIndex]) {
				assignedDifferently++;
			}

			CentroidCluster<T> cluster = clusters.get(clusterIndex);
			cluster.addPoint(p);
			assignments[pointIndex++] = clusterIndex;
		}

		return assignedDifferently;
	}

	/**
	 * Use K-means++ to choose the initial centers.
	 *
	 * @param points
	 *            the points to choose the initial centers from
	 * @return the initial centers
	 */
	private List<CentroidCluster<T>> chooseInitialCenters(final Collection<T> points) {

		// Convert to list for indexed access. Make it unmodifiable, since
		// removal of items
		// would screw up the logic of this method.
		final List<T> pointList = Collections.unmodifiableList(new ArrayList<T>(points));

		// The number of points in the list.
		final int numPoints = pointList.size();

		// Set the corresponding element in this array to indicate when
		// elements of pointList are no longer available.
		final boolean[] taken = new boolean[numPoints];

		// The resulting list of initial centers.
		final List<CentroidCluster<T>> resultSet = new ArrayList<CentroidCluster<T>>();

		// Choose one center uniformly at random from among the data points.
		final int firstPointIndex = random.nextInt(numPoints);

		final T firstPoint = pointList.get(firstPointIndex);

		resultSet.add(new CentroidCluster<T>(firstPoint));

		// Must mark it as taken
		taken[firstPointIndex] = true;

		// To keep track of the minimum distance squared of elements of
		// pointList to elements of resultSet.
		final double[] minDistSquared = new double[numPoints];

		// Initialize the elements. Since the only point in resultSet is
		// firstPoint,
		// this is very easy.
		for (int i = 0; i < numPoints; i++) {
			if (i != firstPointIndex) { // That point isn't considered
				double d = distance(firstPoint, pointList.get(i));
				minDistSquared[i] = d * d;
			}
		}

		while (resultSet.size() < k) {

			// Sum up the squared distances for the points in pointList not
			// already taken.
			double distSqSum = 0.0;
			for (int i = 0; i < numPoints; i++) {
				if (!taken[i]) {
					distSqSum += minDistSquared[i];
				}
			}

			// Add one new data point as a center. Each point x is chosen with
			// probability proportional to D(x)2
			final double r = random.nextDouble() * distSqSum;

			// The index of the next point to be added to the resultSet.
			int nextPointIndex = -1;

			// Sum through the squared min distances again, stopping when
			// sum >= r.
			double sum = 0.0;
			for (int i = 0; i < numPoints; i++) {
				if (!taken[i]) {
					sum += minDistSquared[i];
					if (sum >= r) {
						nextPointIndex = i;
						break;
					}
				}
			}

			// If it's not set to >= 0, the point wasn't found in the previous
			// for loop, probably because distances are extremely small. Just
			// pick
			// the last available point.
			if (nextPointIndex == -1) {
				for (int i = numPoints - 1; i >= 0; i--) {
					if (!taken[i]) {
						nextPointIndex = i;
						break;
					}
				}
			}

			// We found one.
			if (nextPointIndex >= 0) {

				final T p = pointList.get(nextPointIndex);

				resultSet.add(new CentroidCluster<T>(p));

				// Mark it as taken.
				taken[nextPointIndex] = true;

				if (resultSet.size() < k) {
					// Now update elements of minDistSquared. We only have to
					// compute
					// the distance to the new center to do this.
					for (int j = 0; j < numPoints; j++) {
						// Only have to worry about the points still not taken.
						if (!taken[j]) {
							double d = distance(p, pointList.get(j));
							double d2 = d * d;
							if (d2 < minDistSquared[j]) {
								minDistSquared[j] = d2;
							}
						}
					}
				}

			} else {
				// None found --
				// Break from the while loop to prevent
				// an infinite loop.
				break;
			}
		}
		return resultSet;
	}

	/**
	 * Get the point farthest to its cluster center
	 *
	 * @param clusters
	 *            the {@link Cluster}s to search
	 * @return point farthest to its cluster center
	 * @throws ConvergenceException
	 *             if clusters are all empty
	 */
	private T getFarthestPoint(final Collection<CentroidCluster<T>> clusters) {

		double maxDistance = Double.NEGATIVE_INFINITY;
		Cluster<T> selectedCluster = null;
		int selectedPoint = -1;
		for (final CentroidCluster<T> cluster : clusters) {

			// get the farthest point
			final Clusterable center = cluster.getCenter();
			final List<T> points = cluster.getPoints();
			for (int i = 0; i < points.size(); ++i) {
				final double distance = distance(points.get(i), center);
				if (distance > maxDistance) {
					maxDistance = distance;
					selectedCluster = cluster;
					selectedPoint = i;
				}
			}
		}
		return selectedCluster.getPoints().remove(selectedPoint);
	}

	/**
	 * Returns the nearest {@link Cluster} to the given point
	 *
	 * @param clusters
	 *            the {@link Cluster}s to search
	 * @param point
	 *            the point to find the nearest {@link Cluster} for
	 * @return the index of the nearest {@link Cluster} to the given point
	 */
	private int getNearestCluster(final Collection<CentroidCluster<T>> clusters, final T point) {
		double minDistance = Double.MAX_VALUE;
		int clusterIndex = 0;
		int minCluster = 0;
		for (final CentroidCluster<T> c : clusters) {
			final double distance = distance(point, c.getCenter());
			if (distance < minDistance) {
				minDistance = distance;
				minCluster = clusterIndex;
			}
			clusterIndex++;
		}
		return minCluster;
	}

	/**
	 * Computes the centroid for a set of points.
	 *
	 * @param points
	 *            the set of points
	 * @param dimension
	 *            the point dimension
	 * @return the computed centroid for the set of points
	 */
	private Clusterable centroidOf(final Collection<T> points, final int dimension) {
		final double[] centroid = new double[dimension];
		for (final T p : points) {
			final double[] point = p.getPoint();
			for (int i = 0; i < centroid.length; i++) {
				centroid[i] += point[i];
			}
		}
		for (int i = 0; i < centroid.length; i++) {
			centroid[i] /= points.size();
		}
		return new KMeansPoint(centroid);
	}

	private double distance(Clusterable c1, Clusterable c2) {
		double sum = 0d;
		double p1[] = c1.getPoint();
		double p2[] = c2.getPoint();
		for (int i = 0; i < p1.length; i++) {
			sum += Math.pow(p1[i] - p2[i], 2);
		}
		return Math.sqrt(sum);
	}

}