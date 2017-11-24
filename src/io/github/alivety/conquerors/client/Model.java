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
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

import io.github.alivety.conquerors.client.events.CreateModelEvent;
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
	
	protected Spatial makeCylinder(int axisSamples,int radialSamples,float radius,float height) {
		Cylinder cyl=new Cylinder(axisSamples,radialSamples,radius,height);
		return new Geometry(Main.uuid("cylinder"),cyl);
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
	
	/**
	 * MODELS
	 */
	
	public static class NetworkModel extends Model {
		private final CreateModelEvent evt;
		
		public NetworkModel(final CreateModelEvent evt) {
			this.evt = evt;
		}
		
		@Override
		public Spatial build() {
			final Node node = new Node(Main.uuid(this.evt.name));
			for (final int[] shape : this.evt.form) {
				/*
				 * shape[0] = type (0=cube,1=sphere,2=cylindar)
				 * shape[1-4] = RGBA color
				 * shape[5-7] = position
				 * shape[8-*] = shape-specific constructors
				 */
				final ColorRGBA color = new ColorRGBA(shape[1], shape[2], shape[3], shape[4]);
				final Vector3f pos = new Vector3f(shape[5], shape[6], shape[7]);
				Spatial spat;
				if (shape[0]==0)
					spat = this.makeCube(new Vector3f(shape[8], shape[9], shape[10]));
				else if (shape[0]==1)
					spat = this.makeSphere(32, 32, shape[8]);
				else if (shape[0]==2)
					spat=this.makeCylinder(32, 32, shape[8], shape[10]);
				else
					throw new IllegalArgumentException(shape[0]+" is not a valid shape ID");
				this.colorSpatial(spat, color);
				this.positionSpatial(spat, pos);
				node.attachChild(spat);
			}
			node.setLocalTranslation(new Vector3f(this.evt.position[0], this.evt.position[1], this.evt.position[2]));
			return node;
		}
		
	}
	
	@Deprecated
	private static abstract class TeamModel extends Model {
		protected ColorRGBA color;
		
		public TeamModel(final ColorRGBA color) {
			this.color = color;
			Main.out.debug(this.getClass().getName() + ":" + color);
		}
	}
	
	@Deprecated
	public static class CommandCenter extends TeamModel {
		public CommandCenter(final ColorRGBA color) {
			super(color);
		}
		
		@Override
		public Spatial build() {
			final Node node = new Node(Main.uuid("CommandCenter:" + this.color));
			final Spatial cube = this.makeCube(new Vector3f(1, 1, 1));
			this.colorSpatial(cube, this.color);
			node.attachChild(cube);
			return node;
		}
	}
}
