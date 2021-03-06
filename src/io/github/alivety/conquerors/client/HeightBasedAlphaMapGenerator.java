/*
 * Copyright (c) 2009-2016 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.alivety.conquerors.client;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;

/**
 * <p>
 * <code>HeightBasedAlphaMapGenerator</code> is used for generating alpha maps
 * based on a heightmap and given thresholds for the colors.
 * </p>
 * 
 * <p>
 * <b>Default values:</b>
 * <ul>
 * <li>0f - 50f = Red (Tex1)</li>
 * <li>50f - 100f = Green (Tex2)</li>
 * <li>100f - 200f = Blue (Tex3)</li>
 * </ul>
 * </p>
 * 
 * @author Sebastian Teumert
 *         (<a href="https://github.com/NetzwergX">github.com/NetzwergX</a>)
 *
 */
public class HeightBasedAlphaMapGenerator {
	
	/* ----- Variables ----- */
	
	HeightMap heightmap;
	
	WeakReference<Image> alphamap;
	boolean rendered = false;
	
	private float tex1Height = 0;
	private float tex2Height = 50;
	private float tex3Height = 100;
	private float maxHeight = 200;
	
	/* ----- Constructor ----- */
	
	/**
	 * Constructs a new <code>HeightBasedAlphaMapGenerator</code> for the given
	 * heightmap
	 * 
	 * @param heightMap
	 *            the heightmap to be used
	 */
	public HeightBasedAlphaMapGenerator(final HeightMap heightMap) {
		this.setHeightmap(heightMap);
	}
	
	/**
	 * Constructs a new <code>HeightBasedAlphaMapGenerator</code> for the given
	 * heightmap and color thresholds.
	 * 
	 * @param heightMap
	 *            the underlying heightmap
	 * @param tex1Height
	 *            minimal height for tex1 (red)
	 * @param tex2Height
	 *            minimal height for tex2 (green)
	 * @param tex3Height
	 *            minimal height for tex3 (blue)
	 * @param maxHeight
	 *            maximal height (else alpha = 0)
	 */
	public HeightBasedAlphaMapGenerator(final HeightMap heightMap, final float tex1Height, final float tex2Height, final float tex3Height, final float maxHeight) {
		this(heightMap);
		this.setTex1Height(tex1Height);
		this.setTex2Height(tex2Height);
		this.setTex3Height(tex3Height);
		this.setMaxHeight(maxHeight);
	}
	
	/* ----- Methods ----- */
	
	public HeightMap getHeightmap() {
		return this.heightmap;
	}
	
	public void setHeightmap(final HeightMap heightmap) {
		this.rendered = this.rendered && (heightmap == this.heightmap);
		this.heightmap = heightmap;
	}
	
	public float getTex1Height() {
		return this.tex1Height;
	}
	
	public void setTex1Height(final float tex1Height) {
		this.rendered = this.rendered && (this.tex1Height == tex1Height);
		this.tex1Height = tex1Height;
	}
	
	public float getTex2Height() {
		return this.tex2Height;
	}
	
	public void setTex2Height(final float tex2Height) {
		this.rendered = this.rendered && (this.tex2Height == tex2Height);
		this.tex2Height = tex2Height;
	}
	
	public float getTex3Height() {
		return this.tex3Height;
	}
	
	public void setTex3Height(final float tex3Height) {
		this.rendered = this.rendered && (this.tex3Height == tex3Height);
		this.tex3Height = tex3Height;
	}
	
	public float getMaxHeight() {
		return this.maxHeight;
	}
	
	public void setMaxHeight(final float maxHeight) {
		this.rendered = this.rendered && (this.maxHeight == maxHeight);
		this.maxHeight = maxHeight;
	}
	
	/**
	 * Renders and returns the alpha map. Subsequent calls only re-generate the
	 * image if one of the height values are changed, otherwise a cached version
	 * is returned.
	 * 
	 * @return the rendered alpha map
	 */
	public Image renderAlphaMap() {
		if (this.rendered && (this.alphamap.get() != null))
			return this.alphamap.get();
		
		final int height = this.heightmap.getSize();
		final int width = this.heightmap.getSize();
		
		final ByteBuffer data = BufferUtils.createByteBuffer(width * height * 4);
		
		for (int x = 0; x < width; x++)
			for (int z = 0; z < height; z++) {
				
				int alpha = 255, red = 0, green = 0, blue = 0;
				
				final float pointHeight = this.heightmap.getScaledHeightAtPoint(z, width - (x + 1));
				
				if (pointHeight < this.maxHeight) {
					if (pointHeight > this.tex3Height)
						blue = 255;
					else if (pointHeight > this.tex2Height)
						green = 255;
					else if (pointHeight > this.tex1Height)
						red = 255;
					else if (pointHeight < this.tex1Height)
						alpha = 0;
				} else
					alpha = 0;
				data.put((byte) red).put((byte) green).put((byte) blue).put((byte) alpha);
			}
		
		final Image image = new Image(Image.Format.RGBA8, width, height, data);
		this.alphamap = new WeakReference<Image>(image);
		this.rendered = true;
		return image;
	}
}