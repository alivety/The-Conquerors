package io.github.alivety.conquerors.client;

import com.google.common.base.Preconditions;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import io.github.alivety.conquerors.common.Main;

public abstract class Model {
	public static AssetManager assetManager;
	protected Spatial makeCube(Vector3f size) {
		Box box=new Box(size.x,size.y,size.z);
		return new Geometry(Main.uuid("cube"),box);
	}
	
	protected Spatial makeSphere(int zSamples,int radialSamples,float radius) {
		Sphere sphere=new Sphere(zSamples,radialSamples,radius);
		return new Geometry(Main.uuid("sphere"),sphere);
	}
	
	protected void positionSpatial(Spatial spat,Vector3f location) {
		spat.setLocalTranslation(location);
	}
	
	protected void colorSpatial(Spatial spat,ColorRGBA color) {
		Preconditions.checkNotNull(assetManager,"assetManager is not initialized");
		Material mat=new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		spat.setMaterial(mat);
	}
	
	public abstract Spatial build();
}
