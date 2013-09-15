package com.greedygame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetsComic2 {
	
	public static Array<AtlasRegion> COMIC;
	public static TextureRegion METER;
	private static AssetManager manager;
	
	public static boolean allLoaded = false;
	
	public static void load(AssetManager _manager) {
		manager = _manager;
		manager.load("data/comic2.atlas", TextureAtlas.class);
	}
	
	public static void init() {
		allLoaded = true;		
		TextureAtlas _atlasInstruction = manager.get("data/comic2.atlas", TextureAtlas.class);
		COMIC = _atlasInstruction.findRegions("comic");		
		METER = _atlasInstruction.findRegion("indicator");
	}

	public static void unload(){	
		manager.unload("data/comic2.atlas");

	}

}

