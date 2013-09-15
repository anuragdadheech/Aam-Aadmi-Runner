package com.greedygame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetsComicAll {
	
	public static Array<AtlasRegion> COMIC, COMIC2;
	public static TextureRegion METER, METER2;
	private static AssetManager manager;
	
	public static boolean allLoaded = false;
	
	public static void load(AssetManager _manager) {
		manager = _manager;
		manager.load("data/comic.atlas", TextureAtlas.class);
		manager.load("data/comic2.atlas", TextureAtlas.class);
	}
	
	public static void init() {
		allLoaded = true;		
		TextureAtlas _atlasInstruction = manager.get("data/comic.atlas", TextureAtlas.class);
		COMIC = _atlasInstruction.findRegions("comic");		
		METER = _atlasInstruction.findRegion("indicator");
		TextureAtlas _atlasInstruction2 = manager.get("data/comic2.atlas", TextureAtlas.class);
		COMIC2 = _atlasInstruction2.findRegions("comic");		
		METER2 = _atlasInstruction2.findRegion("indicator");
	}

	public static void unload(){	
		manager.unload("data/comic.atlas");
		manager.unload("data/comic2.atlas");
	}

}

