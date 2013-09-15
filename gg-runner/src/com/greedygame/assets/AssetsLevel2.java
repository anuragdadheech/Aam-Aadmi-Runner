package com.greedygame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetsLevel2 {
	public static Array<AtlasRegion> BLAST;
	public static Array<AtlasRegion> BUS, PILLAR, CORRUPT, FLYOVER;
	public static TextureRegion DelhiSky, DelhiHouse;	
	public static Texture  Coin;

	private static AssetManager manager;
	
	public static boolean allLoaded = false;
	
	public static void load(AssetManager _manager) {
		manager = _manager;		
        manager.load("data/delhi2.atlas", TextureAtlas.class);
        manager.load("data/blocks2.atlas", TextureAtlas.class);
        manager.load("data/coin.png", Texture.class);
        manager.load("data/boomanim.atlas", TextureAtlas.class);
	}
	
	public static void init() {
		allLoaded = true;
		
				
		TextureAtlas _atlasDelhi =  manager.get("data/delhi2.atlas", TextureAtlas.class); 
		DelhiSky = _atlasDelhi.findRegion("sky");
		DelhiHouse = _atlasDelhi.findRegion("city");
		
		TextureAtlas _atlasBlast = manager.get("data/boomanim.atlas", TextureAtlas.class);
		BLAST = _atlasBlast.findRegions("boom");
		

		
		TextureAtlas _atlasBlock =  manager.get("data/blocks2.atlas", TextureAtlas.class);
		BUS = _atlasBlock.findRegions("bus");
		PILLAR = _atlasBlock.findRegions("pillar");
		CORRUPT = _atlasBlock.findRegions("leader");
		FLYOVER = _atlasBlock.findRegions("fly");
		
		Coin =  manager.get("data/coin.png", Texture.class);
		
	}

	public static void unload(){	
	      manager.unload("data/delhi2.atlas");
	      manager.unload("data/blocks2.atlas");
	      manager.unload("data/coin.png");
	      manager.unload("data/boomanim.atlas");
	}

}

