package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.greedygame.AbstractScreen;
import com.greedygame.Constant;
import com.greedygame.aap.HudTable;
import com.greedygame.aap.RunnerGame;
import com.greedygame.aap.RunnerTable;
import com.greedygame.assets.AssetsCommon;
import com.greedygame.assets.AssetsLevel1;
import com.greedygame.assets.AssetsLevel2;
import com.greedygame.dialog.PauseDialog;
import com.greedygame.dialog.RetryDialog;
import com.greedygame.dialog.SupportDialog;
import com.greedygame.dialog.WindowDialogListener;
import com.greedygame.scoreoid.ScoreoidConstants;

public class GameScreen extends AbstractScreen implements InputProcessor , GestureListener, WindowDialogListener{
	
	public static final float FRUSTUM_WIDTH = 26;
	public static final float FRUSTUM_HEIGHT = 20;
    public static float SCALE = RunnerGame.VIRTUAL_WIDTH/GameScreen.FRUSTUM_WIDTH;
	static final float BOX_STEP=1/120f;
	static final int  BOX_VELOCITY_ITERATIONS=8;
	static final int BOX_POSITION_ITERATIONS=3;
    
	public static float toCamera(float x){
		return x/GameScreen.SCALE;
	}
	
	public static float toWorld(float y){
		return y*(GameScreen.SCALE);
	}
	
    private enum Stat {
        GAME_READY, GAME_RUNNING,  GAME_PAUSE,GAME_PAUSED, GAME_OVER, GAME_COMELETE
    }
    private Stat gameStat;
	int state;
	private int level;
	
	private Stage stage, hudStage;
	private RunnerTable runnerTable;
	private HudTable hudTable;

	private RetryDialog retryWindow;
	private SupportDialog supportWindow;
	private PauseDialog pauseDialog;

	World world =null;
    Box2DDebugRenderer debugRenderer;  
	public GameScreen(RunnerGame game, int level) {
		super(game);
		this.level = level;
		world = new World(new Vector2(0, -200), true);  
        debugRenderer = new Box2DDebugRenderer();  
    }
	

	@Override
	public void show() {
		stage = new Stage();	
        hudStage = new Stage(RunnerGame.VIRTUAL_WIDTH, RunnerGame.VIRTUAL_HEIGHT, true,stage.getSpriteBatch());
			
		runnerTable = new RunnerTable(this, RunnerGame.VIRTUAL_WIDTH, RunnerGame.VIRTUAL_HEIGHT, level);
		hudTable = new HudTable(runnerTable,RunnerGame.VIRTUAL_WIDTH, RunnerGame.VIRTUAL_HEIGHT);
		
		hudStage.addActor(hudTable);
		stage.addActor(runnerTable);		 
		supportWindow = new SupportDialog(this, hudStage);
        retryWindow = new RetryDialog(this, hudStage);
        pauseDialog = new PauseDialog(this, hudStage);
        
        init();
	}
	
	@Override
	public void init() {
		InputMultiplexer im = new InputMultiplexer(this);
		im.addProcessor(new GestureDetector(this));
		Gdx.input.setInputProcessor(im);	
		
        Gdx.input.setCatchBackKey(true);        
        AssetsCommon.BACKGROUND_MUSIC.play();
        gameStat = Stat.GAME_READY;
		game.analyticsEngine.sendView("GameScreen");		
		tryScoreSend = false;
		
	}



	@Override
	public void resize(int width, int height) {		
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;
        hudStage.setViewport(width , height, true);
        retryWindow.setPosition((width-retryWindow.getWidth())/2, (height-retryWindow.getHeight())/2);
        supportWindow.setPosition((width-supportWindow.getWidth())/2, (height-supportWindow.getHeight())/2);
		
        hudTable.reset(width, height);
		runnerTable.reset(width, height);
        
		float fheight =  (FRUSTUM_WIDTH * height/width);
        stage.setViewport(FRUSTUM_WIDTH , fheight, true);
        stage.getCamera().position.set(FRUSTUM_WIDTH/2, fheight/2, 0);
	}

	boolean showRetryDialog = false;
	boolean keyDown = false;
	float accumulator;
	boolean pausedPress = false;
	
	void updateState(){
		if(this.runnerTable.getMan().isKilled()){
			this.gameStat = Stat.GAME_OVER;
		}else if(pausedPress){
			this.gameStat = Stat.GAME_PAUSE;
		}else if(RunnerGame.score.percentBlock() >= 1){
			this.gameStat = Stat.GAME_COMELETE;
		}else{
			this.gameStat = Stat.GAME_RUNNING;
		}
	}
	
	@Override
	public void render(float delta) {
	   Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	   stage.act();
	   stage.draw();

	   hudStage.act(delta);
	   hudStage.draw();
	
	   if(RunnerGame.score.percentBlock() >= 1){
			this.gameStat = Stat.GAME_COMELETE;
		}
	   
	   //updateState();
		accumulator+=delta;
	    while(accumulator>BOX_STEP){
	      world.step(BOX_STEP,BOX_VELOCITY_ITERATIONS,BOX_POSITION_ITERATIONS);
	      accumulator-=BOX_STEP;
	    }
	    	    
	    //debugRenderer.render(world, stage.getCamera().combined);  
	    
		if(gameStat == Stat.GAME_PAUSE){
			this.pause();
		}
		
		

	}


	@Override 
    public void hide() {
    	Gdx.input.setInputProcessor(null);
    	runnerTable.remove();
    	stage.clear();
    	stage = null;    
      	if(level==1){
    		AssetsLevel1.unload();
      	}else if(level==2){
      		AssetsLevel2.unload();
      	}
      	AssetsCommon.BACKGROUND_MUSIC.pause();
    }
	
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	
	@Override 
	public boolean touchDown(float x, float y, int pointer, int button) {
		if(this.gameStat == Stat.GAME_COMELETE){
			this.game.setScreen(new SelectScreen(game));
		}
		if(x > Gdx.graphics.getWidth()/2){
			if((x > Gdx.graphics.getWidth() - AssetsCommon.PAUSE.getRegionWidth()) &&
					(y < (AssetsCommon.PAUSE.getRegionWidth()+20)) ){
				this.pause();
			}else{
				runnerTable.jumpMan();
			}
		}else {
			runnerTable.duckMan();
		}
		return false;
	}

	@Override public boolean longPress(float x, float y) {
		return false;
	}
	
	public static boolean tryScoreSend = false;
	boolean retry = false;
	public void showRetry(){
		if(tryScoreSend==false){
		this.gameStat = Stat.GAME_OVER;
		AssetsCommon.BACKGROUND_MUSIC.pause();
		if(!retryWindow.isVisible()){
			retryWindow.show();
		}
				
		if(RunnerGame.score.isConnected()){
			long stored_score = RunnerGame.setting.getLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE);

			Gdx.app.log("trySend", stored_score+","+(int)RunnerGame.score.getScore());
			if((int)RunnerGame.score.getScore()> stored_score){
				
				RunnerGame.scoreoid.CreateScore(RunnerGame.score.getUsername(), (long)RunnerGame.score.getScore());
			}
			
		}

		tryScoreSend = true;
		}
	}
	
	@Override public void resume() {

	}
	
	boolean trypause = false;
	@Override 
	public void pause() {
		this.gameStat = Stat.GAME_PAUSE;
		if(trypause == false){
			AssetsCommon.BACKGROUND_MUSIC.pause();
			pauseDialog.show();
			this.runnerTable.pause();
			this.hudTable.pause();
			trypause = true;
		}
		
		if(supportWindow.isVisible()){
			pauseDialog.hide();
		}else{
			pauseDialog.show();
		}
	}
	@Override public void dispose() {}	
	@Override public boolean tap(float x, float y, int count, int button) {	return false;}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {return false;}
	@Override public boolean zoom(float initialDistance, float distance) {return false;}
	@Override public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {return false;}
	
	@Override
	public void onButtonClicked(String name)  {	
		if(name == "resume"){
			this.resumeClick();
			AssetsCommon.BACKGROUND_MUSIC.play();
		}else if(name == "retry"){
			//play_again
			retryWindow.hide();
			init();
			runnerTable.restart();
			hudTable.restart();
			tryScoreSend = false;
			gameStat = Stat.GAME_RUNNING;
			AssetsCommon.BACKGROUND_MUSIC.play();
		}else if(name == "support"){
			//support
			retryWindow.hide();
			pauseDialog.hide();
			supportWindow.show();
		}else if(name == "score"){
			//leaderboard
	        game.analyticsEngine.sendView("Leaderboard");
	        
	        Gdx.app.log("leaderboard","show score");
	        RunnerGame.scoreoid.OpenHighScoresAround(RunnerGame.score);
		}else if(name == "donate"){
			//donate
			Gdx.net.openURI(Constant.LINK_AAP_DONATION);
		}else if(name == "vote"){
			//vote
			Gdx.net.openURI(Constant.LINK_GG_ENGAGEMENT);
		}else if(name == "share"){
			//share
			//Gdx.net.openURI(Constant.LINK_FB_SHARE);
			RunnerGame.facebook.inviteDialog();
		}
	}

	private void resumeClick() {
		pauseDialog.hide();
		this.runnerTable.resume();
		this.hudTable.resume();
		pausedPress = false;
		trypause = false;
		gameStat = Stat.GAME_RUNNING;
	}

	public World getWorld() {		
		return this.world;
	}
	
	public Camera getCamera() {		
		return this.stage.getCamera();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ESCAPE || keycode == Keys.BACK){
			//this.game.setScreen(new MenuScreen(game));
			this.pause();
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCancelClicked(String name) {
		System.out.println("cancel" +name);
		if(name.equals("pause")){
			this.pauseDialog.hide();
			this.game.setScreen(new MenuScreen(game));
		}else if(name.equals("support")){
			this.supportWindow.hide();
			if(this.gameStat == Stat.GAME_PAUSE){
				pauseDialog.show();
			}else if(this.gameStat == Stat.GAME_OVER){
				retryWindow.show();
			}
		}
		
	}


}