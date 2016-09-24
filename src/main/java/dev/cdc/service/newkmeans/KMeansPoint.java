package dev.cdc.service.newkmeans;

public class KMeansPoint implements Clusterable {

	private String id;
	private double point[];
	private String text;

	public KMeansPoint(double[] point) {
		super();
		this.point = point;
	}

	public KMeansPoint(String id, String text, double[] point) {
		super();
		this.id = id;
		this.point = point;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double[] getPoint() {
		return point;
	}

	public void setPoint(double[] point) {
		this.point = point;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Point [id=" + id + ", text=" + text + "]";
	}

}