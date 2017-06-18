package io.github.alivety.conquerors.client;

import com.google.common.base.Preconditions;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import io.github.alivety.conquerors.common.Main;

public abstract class Model {
	public static AssetManager assetManager;
	
	protected Spatial makeCube(final Vector3f size) {
		final Box box = new Box(size.x, size.y, size.z);
		return new Geometry(Main.uuid("cube"), box);
	}
	
	protected Spatial makeSphere(final int zSamples, final int radialSamples, final float radius) {
		final Sphere sphere = new Sphere(zSamples, radialSamples, radius);
		return new Geometry(Main.uuid("sphere"), sphere);
	}
	
	protected void positionSpatial(final Spatial spat, final Vector3f location) {
		spat.setLocalTranslation(location);
	}
	
	protected void colorSpatial(final Spatial spat, final ColorRGBA color) {
		Preconditions.checkNotNull(Model.assetManager, "assetManager is not initialized");
		final Material mat = new Material(Model.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		spat.setMaterial(mat);
	}
	
	public abstract Spatial build();
	
	private static abstract class TeamModel extends Model {
		protected ColorRGBA color;
		public TeamModel(ColorRGBA color) {
			this.color=color;
			Main.out.debug(this.getClass().getName()+":"+color);
		}
	}
	
	
	/**
	 * MODELS
	 */
	
	public static class CommandCenter extends TeamModel {
		public CommandCenter(ColorRGBA color) {
			super(color);
		}

		@Override
		public Spatial build() {
			Node node=new Node(Main.uuid("CommandCenter:"+color));
			Spatial cube=makeCube(new Vector3f(1,1,1));
			colorSpatial(cube, color);
			node.attachChild(cube);
			return node;
		}
	}
}
