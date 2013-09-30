package com.greedygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class AssetsCommon {
	//Buttons Skins
	public static TextureRegion IconPlay, IconSupport, IconInstruction, IconVote, IconDonate, IconScore, IconShare, IconRetry;
	//HUD
	public static TextureRegion SUPER_ON_L1, SUPER_OFF_L1, SUPER_ON_L2, SUPER_OFF_L2, ONLINE, OFFLINE, PAUSE, BAR1, BAR2;
	
	public static TextureRegion IconLevel1, IconLevel2, IconLock;
	//Man
	public static Array<AtlasRegion> MAN_RUN, MAN_JUMP, MAN_ROLL, MAN_ATTACK;
	
	public static TextureRegion BACK_DROP;
	public static BitmapFont FONT;
	public static Skin SKIN;
	public static Sound TICK_SOUND;
	public static Music BACKGROUND_MUSIC;

	private static TextureAtlas SkinAtlas;
	private static AssetManager manager;
	
	public static boolean allLoaded = false;
	
	public static void load(AssetManager _manager) {
		manager = _manager;

		manager.load("skins/uiskin.atlas", TextureAtlas.class);
		manager.load("skins/icons.atlas", TextureAtlas.class);
		manager.load("data/level_select.atlas", TextureAtlas.class);
		manager.load("data/hud.atlas", TextureAtlas.class);
        manager.load("data/arvind.atlas", TextureAtlas.class);
        manager.load("sound/runner.mp3", Music.class);
        manager.load("sound/tick.mp3", Sound.class);
        manager.load("data/jhaaduanim.atlas", TextureAtlas.class);
	}
	
	public static void init() {
		allLoaded = true;
		SkinAtlas =  manager.get("skins/uiskin.atlas", TextureAtlas.class);
		
		SKIN = new Skin(Gdx.files.internal("skins/uiskin.json"), SkinAtlas);
		FONT = SKIN.getFont("default-font");
		
		TextureAtlas _scoreAtlas = manager.get("data/hud.atlas", TextureAtlas.class);
		SUPER_OFF_L1 = _scoreAtlas.findRegion("superoff1");
		SUPER_ON_L1 = _scoreAtlas.findRegion("superon1");
		SUPER_OFF_L2 = _scoreAtlas.findRegion("superoff2");
		SUPER_ON_L2 = _scoreAtlas.findRegion("superon2");
		ONLINE = _scoreAtlas.findRegion("online");
		OFFLINE = _scoreAtlas.findRegion("offline");
		PAUSE = _scoreAtlas.findRegion("pause");
		BAR1 = _scoreAtlas.findRegion("bar1");
		BAR2 = _scoreAtlas.findRegion("bar2");
		
		TextureAtlas _atlasIcon = manager.get("skins/icons.atlas", TextureAtlas.class);
		IconPlay = _atlasIcon.findRegion("play");
		IconSupport = _atlasIcon.findRegion("support"); 
		IconInstruction = _atlasIcon.findRegion("instruction"); 
		IconVote = _atlasIcon.findRegion("vote"); 
		IconDonate = _atlasIcon.findRegion("donate");
		IconScore = _atlasIcon.findRegion("score");
		IconShare = _atlasIcon.findRegion("share");
		IconRetry = _atlasIcon.findRegion("retry");
		
		TextureAtlas _atlasLevel = manager.get("data/level_select.atlas");
		IconLevel1 = _atlasLevel.findRegion("level1");
		IconLevel2 = _atlasLevel.findRegion("level2");
		IconLock = _atlasLevel.findRegion("lock");
		
				
		TextureAtlas _atlasMenu1 = manager.get("data/backdrop.atlas", TextureAtlas.class);
		BACK_DROP = _atlasMenu1.findRegion("loading");
		
		TextureAtlas _atlasDodle = manager.get("data/arvind.atlas", TextureAtlas.class);
		MAN_RUN = _atlasDodle.findRegions("run");	
		MAN_JUMP = _atlasDodle.findRegions("run");	
		MAN_ROLL = _atlasDodle.findRegions("roll");
		TextureAtlas _atlasAttack = manager.get("data/jhaaduanim.atlas", TextureAtlas.class);
		MAN_ATTACK = _atlasAttack.findRegions("jhaadu");		
		

		TICK_SOUND = manager.get("sound/tick.mp3", Sound.class);
		BACKGROUND_MUSIC =  manager.get("sound/runner.mp3", Music.class);
	}

	public static void unload(){	
		manager.unload("skins/uiskin.atlas");
		manager.unload("skins/icons.atlas");
		manager.unload("data/hud.atlas");
        manager.unload("data/arvind.atlas");
		manager.unload("data/instruction.atlas");	
        manager.unload("data/boomanim.atlas");
        manager.unload("sound/runner.mp3");
	}

}

