package io.github.alivety.conquerors.client;

import java.lang.reflect.Method;
import java.util.Queue;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.material.Material;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;

import io.github.alivety.conquerors.common.Main;

public class ConquerorsSpatial extends Spatial {
	private Spatial spat;
	private String ownerSpatialID;
	
	public ConquerorsSpatial(AssetManager assetManager,String model,String material,String spatialID) {
		spat=assetManager.loadModel(model);
		Material mat=new Material(assetManager,material);
		spat.setMaterial(mat);
		spat.setName(spatialID);
	}
	
	/* Shorthand method */
	public static Spatial newSpatial(AssetManager assetManager,String model,String material,String spatialID) {
		return new ConquerorsSpatial(assetManager,model,material,spatialID);
	}
	
	public String getOwner() {
		return ownerSpatialID;
	}
	
	public void setOwner(String owner) {
		this.ownerSpatialID=owner;
	}
	
	public int collideWith(Collidable arg0, CollisionResults arg1) throws UnsupportedCollisionException {
		return spat.collideWith(arg0, arg1);
	}

	@Override
	protected void breadthFirstTraversal(SceneGraphVisitor arg0, Queue<Spatial> arg1) {
		try {
			Method m=spat.getClass().getMethod("breadthFirstTraversal", SceneGraphVisitor.class, Queue.class);
			m.invoke(spat, arg0, arg1);
		} catch (Exception e) {
			Main.handleError(e);
		}
	}

	@Override
	public void depthFirstTraversal(SceneGraphVisitor arg0) {
		spat.depthFirstTraversal(arg0);
	}

	@Override
	public int getTriangleCount() {
		return spat.getTriangleCount();
	}

	@Override
	public int getVertexCount() {
		return spat.getVertexCount();
	}

	@Override
	public void setModelBound(BoundingVolume arg0) {
		spat.setModelBound(arg0);
	}

	@Override
	public void updateModelBound() {
		spat.updateModelBound();
	}
}
