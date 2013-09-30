package com.greedygame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetsLevel1 {
	public static Array<AtlasRegion> Hypocrite, WARING, BARRICADE, WATERGUN, CROWD;
	public static TextureRegion DelhiSky, DelhiHouse;
	
	public static AtlasRegion  CELEBRATION;

	private static AssetManager manager;
	
	public static boolean allLoaded = false;
	
	public static void load(AssetManager _manager) {
		manager = _manager;
		manager.load("data/crowd.atlas" , TextureAtlas.class);		
        manager.load("data/delhi1.atlas", TextureAtlas.class);
        manager.load("data/blocks1.atlas", TextureAtlas.class);
        manager.load("data/levelover.atlas", TextureAtlas.class);
	}
	
	public static void init() {
		allLoaded = true;
		
				
		TextureAtlas _atlasDelhi =  manager.get("data/delhi1.atlas", TextureAtlas.class); 
		DelhiSky = _atlasDelhi.findRegion("sky");
		DelhiHouse = _atlasDelhi.findRegion("city");
		
		TextureAtlas _atlasBlock =  manager.get("data/blocks1.atlas", TextureAtlas.class);
		Hypocrite = _atlasBlock.findRegions("redman");
		WARING = _atlasBlock.findRegions("no_entry");
		BARRICADE = _atlasBlock.findRegions("police");
		WATERGUN = _atlasBlock.findRegions("watergun");
		
		TextureAtlas _atlasCrowd = manager.get("data/crowd.atlas" , TextureAtlas.class);
		CROWD = _atlasCrowd.findRegions("crowd");
		TextureAtlas _atlasCelebration = manager.get("data/levelover.atlas", TextureAtlas.class);
		CELEBRATION =  _atlasCelebration.findRegion("congo");
	}

	public static void unload(){	
		manager.unload("data/crowd.atlas");
        manager.unload("data/delhi1.atlas");
        manager.unload("data/blocks1.atlas");
        manager.unload("data/levelover.atlas");
	}

}

