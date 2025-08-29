package com.example;

import java.util.Objects;

public class Complex {
	public double x;
	public double y;

	public Complex() {
		this.x = 0.0D;
		this.y = 0.0D;
	}

	public Complex(double var1, double var3) {
		this.x = var1;
		this.y = var3;
	}

	public Complex add(Complex var1) {
		return new Complex(this.x + var1.x, this.y + var1.y);
	}

	double arg() {
		if (this.x == 0.0D && this.y >= 0.0D) {
			return 1.5707963267948966D;
		} else if (this.x == 0.0D && this.y < 0.0D) {
			return 4.71238898038469D;
		} else if (this.x > 0.0D && this.y >= 0.0D) {
			return Math.atan(this.y / this.x);
		} else if (this.x < 0.0D && this.y >= 0.0D) {
			return 3.141592653589793D + Math.atan(this.y / this.x);
		} else {
			return this.x < 0.0D && this.y < 0.0D ? 3.141592653589793D + Math.atan(this.y / this.x)
					: 6.283185307179586D + Math.atan(this.y / this.x);
		}
	}

	Complex complement() {
		return new Complex(-this.x, -this.y);
	}

	protected static Complex ConvertToNormal(double var0, double var2) {
		return new Complex(var0 * Math.cos(var2), var0 * Math.sin(var2));
	}

	double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	Complex sqrt() {
		double var1 = this.length();
		double var3 = 0.0D;
		if (this.y > 0.0D) {
			var3 = 1.0D;
		}

		if (this.y < 0.0D) {
			var3 = -1.0D;
		}

		return new Complex(Math.sqrt((var1 + this.x) / 2.0D), Math.sqrt((var1 - this.x) / 2.0D) * var3);
	}

	public Complex sub(Complex var1) {
		return new Complex(this.x - var1.x, this.y - var1.y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
				&& Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}

	@Override
	public String toString() {
		return "Complex [x=" + x + ", y=" + y + "]";
	}
	
	

}
