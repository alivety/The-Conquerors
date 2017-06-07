package io.github.alivety.conquerors;

public class Vector {
	private final float x,y,z;
	public Vector(float x,float y,float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vector add(float ax,float ay,float az) {
		return new Vector(x+ax,y+ay,z+az);
	}
	
	public Vector add(Vector v) {
		return this.add(v.x,v.y,v.z);
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	
	public Vector setX(float nx) {
		return new Vector(nx,y,z);
	}
	
	public Vector setY(float ny) {
		return new Vector(x,ny,z);
	}
	
	public Vector setZ(float nz) {
		return new Vector(x,y,nz);
	}
	
	public double distance(Vector v) {
		float x1=x,y1=y,z1=z;
		float x2=v.x,y2=v.y,z2=v.z;
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) + Math.pow(z2-z1, 2));
	}
	
	public String toString() {
		return "("+x+","+y+","+z+")";
	}
}
