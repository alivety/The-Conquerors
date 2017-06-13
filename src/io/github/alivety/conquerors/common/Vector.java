package io.github.alivety.conquerors.common;

public class Vector {
	private final float x, y, z;

	public Vector(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector add(final float ax, final float ay, final float az) {
		return new Vector(this.x + ax, this.y + ay, this.z + az);
	}

	public Vector add(final Vector v) {
		return this.add(v.x, v.y, v.z);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public Vector setX(final float nx) {
		return new Vector(nx, this.y, this.z);
	}

	public Vector setY(final float ny) {
		return new Vector(this.x, ny, this.z);
	}

	public Vector setZ(final float nz) {
		return new Vector(this.x, this.y, nz);
	}

	public double distance(final Vector v) {
		final float x1 = this.x, y1 = this.y, z1 = this.z;
		final float x2 = v.x, y2 = v.y, z2 = v.z;
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + "," + this.z + ")";
	}
}