package com.greedygame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Assets {
	
	public static Array<AtlasRegion> MAN_RUN, MAN_JUMP, MAN_ROLL, MAN_ATTACK;
	public static Array<AtlasRegion> BAR;
	public static Array<AtlasRegion> BLAST, INSTRUCTION;
	public static Array<AtlasRegion> BUS, PILLAR, CORRUPT, FLYOVER;
	
	public static TextureRegion DelhiSky, DelhiHouse;
	public static TextureRegion BACK_DROP;
	
	public static Texture Debug;
	public static AtlasRegion RETRYBOX, MENUBOX, SUPPORTBOX;
	public static Texture  Coin;	
	public static BitmapFont Font;
	public static Sound Tick, BACKGROUND;
	
	private static AssetManager manager;
	
	public static void load(AssetManager _manager) {
		manager = _manager;	
		manager.load("data/retrybox.atlas", TextureAtlas.class);
		manager.load("data/menubox.atlas", TextureAtlas.class);
		manager.load("data/supportbox.atlas", TextureAtlas.class);
		manager.load("data/backdrop.atlas", TextureAtlas.class);
		manager.load("data/instruction.atlas", TextureAtlas.class);	
        manager.load("data/doodle.atlas", TextureAtlas.class);
        manager.load("data/jhaaduanim.atlas", TextureAtlas.class);
        manager.load("data/delhi.atlas", TextureAtlas.class);
        manager.load("data/boomanim.atlas", TextureAtlas.class);
        manager.load("data/bar.atlas", TextureAtlas.class);
        manager.load("data/blocks.atlas", TextureAtlas.class);
        manager.load("data/coin.png", Texture.class);
        manager.load("debug.png", Texture.class);
        manager.load("font/font.fnt", BitmapFont.class);
        manager.load("sound/tick.wav", Sound.class);        
        manager.load("sound/runner.mp3", Sound.class);

	}
	public static void init() {
		TextureAtlas _atlasBox =  manager.get("data/retrybox.atlas", TextureAtlas.class); 
		RETRYBOX = _atlasBox.findRegion("restart");
		
		TextureAtlas _menuBox =  manager.get("data/menubox.atlas", TextureAtlas.class); 
		MENUBOX = _menuBox.findRegion("menu");
		
		TextureAtlas _supportBox =  manager.get("data/supportbox.atlas", TextureAtlas.class); 
		SUPPORTBOX = _supportBox.findRegion("support");
		
		TextureAtlas _atlasMenu1 = manager.get("data/backdrop.atlas", TextureAtlas.class);
		BACK_DROP = _atlasMenu1.findRegion("loading");
		
		TextureAtlas _atlasDodle = manager.get("data/doodle.atlas", TextureAtlas.class);
		MAN_RUN = _atlasDodle.findRegions("run");	
		MAN_JUMP = _atlasDodle.findRegions("run");	
		MAN_ROLL = _atlasDodle.findRegions("roll");
		
		TextureAtlas _atlasAttack = manager.get("data/jhaaduanim.atlas", TextureAtlas.class);
		MAN_ATTACK = _atlasAttack.findRegions("sweep");
		
		TextureAtlas _atlasDelhi =  manager.get("data/delhi.atlas", TextureAtlas.class); 
		DelhiSky = _atlasDelhi.findRegion("sky");
		DelhiHouse = _atlasDelhi.findRegion("city");
		
		TextureAtlas _atlasBlast = manager.get("data/boomanim.atlas", TextureAtlas.class);
		BLAST = _atlasBlast.findRegions("boom");
		
		TextureAtlas _atlasBar = manager.get("data/bar.atlas", TextureAtlas.class);
		BAR = _atlasBar.findRegions("bar");
		
		TextureAtlas _atlasInstruction = manager.get("data/instruction.atlas", TextureAtlas.class);
		INSTRUCTION = _atlasInstruction.findRegions("instruction");
		
		TextureAtlas _atlasBlock =  manager.get("data/blocks.atlas", TextureAtlas.class);
		BUS = _atlasBlock.findRegions("bus");
		PILLAR = _atlasBlock.findRegions("pillar");
		CORRUPT = _atlasBlock.findRegions("leader");
		FLYOVER = _atlasBlock.findRegions("fly");
		
		Coin =  manager.get("data/coin.png", Texture.class);		
		Debug =  manager.get("debug.png", Texture.class);
		Font =  manager.get("font/font.fnt", BitmapFont.class);
		
		Tick = manager.get("sound/tick.wav", Sound.class);
		BACKGROUND = manager.get("sound/runner.mp3", Sound.class);		
	}

	public static void unload(){	
		manager.unload("data/retrybox.atlas");
		manager.unload("data/menubox.atlas");
		manager.unload("data/supportbox.atlas");
		manager.unload("data/backdrop.atlas");		
		manager.unload("data/instruction.atlas");	
        manager.unload("data/doodle.atlas");
        manager.unload("data/jhaaduanim.atlas");
        manager.unload("data/delhi.atlas");
        manager.unload("data/boomanim.atlas");
        manager.unload("data/bar.atlas");
        manager.unload("data/blocks.atlas");
        manager.unload("data/coin.png");
        manager.unload("debug.png");
        manager.unload("font/font.fnt");
        manager.unload("sound/tick.wav");        
        manager.unload("sound/runner.mp3");	
	}

}

