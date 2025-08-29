package com.example;

public class CardioidToRect {
	private double fullAngle = Math.PI;

	private double l = 0;
	private double r = 1;
	private double t = 1;
	private double b = 0;
	
	private boolean logScale = true;
	
	public CardioidToRect(double fullAngle, double l, double r, double t, double b) {
		super();
		this.fullAngle = fullAngle;
		this.l = l;
		this.r = r;
		this.t = t;
		this.b = b;
	}
	
	public CardioidToRect(double l, double r, double t, double b) {
		super();
		this.l = l;
		this.r = r;
		this.t = t;
		this.b = b;
	}
	
	public CardioidToRect() {
		super();
	}

	public Complex toCardioid(Complex p) {
		double dx = p.x - l;
		double dy = p.y - b;

		double nx = dx / (r - l);
		double ny = dy / (t - b);
		
		if (logScale) {
			nx = Math.log(1 + nx * (Math.E - 1));
		}
		
		double rs = nx;
		double phi = fullAngle * ny;

		double rm = 1 / 2D;

		double cos = Math.cos(phi);
		double sin = Math.sin(phi);
		
		double r = rs * rm * (1 + cos);
		double x = rs * 1 / 4D - r * cos;
		double y = r * sin;

		return new Complex(x, y);
	}

}
