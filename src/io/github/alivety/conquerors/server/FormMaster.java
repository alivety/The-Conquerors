package io.github.alivety.conquerors.server;

import com.jme3.math.ColorRGBA;

public class FormMaster {
	public static float[][] getCommandCenter(final ColorRGBA c) {
		/*
		 * return new float[][]{ { 0,//cube c.r,//color red c.g,//color green
		 * c.b,//color blue c.a,//alpha 0,//pos x (left-right) 0,// pos y
		 * (up-down) 0,//pos z (forward-backward) 1,//width 1,//height 1//length
		 * }, { 0, 255, 255, 255, 1, 0f, 1.25f, 0, 1, 0.25f, 0.25f }, { 0, 255,
		 * 255, 255, 1, 0f, 1.25f, 0, 0.25f, 0.25f, 1 }, { 0, 255, 255, 255, 1,
		 * 1.25f, 0, 0, 0.25f, 1.50f, 0.25f }, { 0, 255, 255, 255, 1, -1.25f, 0,
		 * 0, 0.25f, 1.50f, 0.25f }, { 0, 255, 255, 255, 1, 0, 0f, 1.25f, 0.25f,
		 * 1.50f, 0.25f }, { 0, 255, 255, 255, 1, 0, 0, -1.25f, 0.25f, 1.50f,
		 * 0.25f } };
		 */
		return new float[][] { { 0 }, // gravity non-affecting
				{ 0, c.r, c.g, c.b, c.a, 0, 0, 0, 1.5f, 0.45f, 1.5f }, { 0, 255, 255, 255, 1, 2.5f, 0, -0.5f, 1, 0.5f, 2.75f }, { 0, 255, 255, 255, 1, -2.5f, 0, -0.5f, 1, 0.5f, 2.75f }, { 0, c.r, c.g, c.b, c.a, 2.5f, 0.60f, -0.5f, 1, 0.1f, 2.75f }, { 0, c.r, c.g, c.b, c.a, -2.5f, 0.60f, -0.5f, 1, 0.1f, 2.75f }, { 0, 255, 255, 255, 1, 2.5f, 1.20f, -0.5f, 1, 0.5f, 2.75f }, { 0, 255, 255, 255, 1, -2.5f, 1.20f, -0.5f, 1, 0.5f, 2.75f }, };
	}
	
	public static float[][] getLightSolider(final ColorRGBA c) {
		return new float[][] { { 1 }, // gravity affectong
				{ 0, 139 / 255f, 69 / 255f, 19 / 255f, 1, 0, 0, 0, 0.25f, 0.4f, 0.25f, 0 }, { 0, 139 / 255.0f, 69 / 255.0f, 19 / 255.0f, 1, 0.5f, 0, 0, 0.25f, 0.4f, 0.25f, 0 }, { 0, c.r, c.g, c.b, c.a, 0.25f, 0.9f, 0, 0.5f, 0.5f, 0.25f, 0 }, { 0, 139 / 255.0f, 69 / 255.0f, 19 / 255.0f, 1, -0.50f, 1.1f, 0, 0.25f, 0.25f, 0.25f, 0
				
				}, { 0, 139 / 255.0f, 69 / 255.0f, 19 / 255.0f, 1, 1f, 1.1f, 0, 0.26f, 0.25f, 0.25f, 0
				
				}, { 0, c.r, c.g, c.b, c.a, 0.25f, 1.50f, 0, 0.25f, 0.25f, 0.25f, 0 } };
	}
	
	public static float[][] getHeavySolider(final ColorRGBA c) {
		return new float[][] { { 1 }, // gravity affecting
				{ 0, 139 / 255f, 69 / 255f, 19 / 255f, 1, 0, 0, 0, 0.25f * 1.1f, 0.4f * 1.1f, 0.25f * 1.1f, }, { 0, 139 / 255.0f, 69 / 255.0f, 19 / 255.0f, 1, 0.5f * 1.1f, 0, 0, 0.25f * 1.1f, 0.4f * 1.1f, 0.25f * 1.1f }, { 0, c.r, c.g, c.b, c.a, 0.25f * 1.1f, 0.9f * 1.1f, 0, 0.5f * 1.1f, 0.5f * 1.1f, 0.25f * 1.1f }, { 0, 139 / 255.0f, 69 / 255.0f, 19 / 255.0f, 1, -0.50f * 1.1f, 1.1f * 1.1f, 0, 0.25f * 1.1f, 0.25f * 1.1f, 0.25f * 1.1f }, { 0, 139 / 255.0f, 69 / 255.0f, 19 / 255.0f, 1, 1.1f, 1.1f * 1.1f, 0, 0.26f * 1.1f, 0.25f * 1.1f, 0.25f * 1.1f }, { 0, c.r, c.g, c.b, c.a, 0.25f, 1.50f, 0, 0.25f, 0.25f, 0.25f, } };
	}
}
