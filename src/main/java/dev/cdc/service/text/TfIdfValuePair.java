package dev.cdc.service.text;

public class TfIdfValuePair {
	private Double tfidfDoc1;
	private Double tfidfDoc2;

	public TfIdfValuePair() {
		super();
	}

	public TfIdfValuePair(Double tfidfDoc1, Double tfidfDoc2) {
		super();
		this.tfidfDoc1 = tfidfDoc1;
		this.tfidfDoc2 = tfidfDoc2;
	}

	public Double getTfidfDoc1() {
		return tfidfDoc1;
	}

	public void setTfidfDoc1(Double tfidfDoc1) {
		this.tfidfDoc1 = tfidfDoc1;
	}

	public Double getTfidfDoc2() {
		return tfidfDoc2;
	}

	public void setTfidfDoc2(Double tfidfDoc2) {
		this.tfidfDoc2 = tfidfDoc2;
	}

}
