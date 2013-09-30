package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.greedygame.AbstractScreen;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerGame;
import com.greedygame.assets.AssetsComic1;
import com.greedygame.assets.AssetsComic2;
import com.greedygame.assets.AssetsComicAll;
import com.greedygame.assets.AssetsCommon;
import com.greedygame.assets.AssetsLevel1;
import com.greedygame.assets.AssetsLevel2;

public class LoadingScreen extends AbstractScreen  {

    private Stage stage;
    private Image screenBg;

    Image LoadingQuote;
    Image aapLogo, ggLogo;
    public static TextureRegion loading;
    private float percent;
    private Mesh squareMesh;
    private int ScreenCode;
    private boolean showFBdialog;
    private boolean isLoaded;
    
    public LoadingScreen(RunnerGame myGame, int ScreenCode) {
        super(myGame);
        squareMesh = new Mesh(true, 4, 4,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));        
        this.ScreenCode = ScreenCode;
    }
    
    public void drawBar(float width, float height){
        squareMesh.setVertices(new float[] {
                0, 0, 0, Color.toFloatBits(254, 100, 0, 255),
                width, 0, 0, Color.toFloatBits(254, 100, 0, 255),
                0, height, 0, Color.toFloatBits(254,100, 0, 255),
                width, height, 0, Color.toFloatBits(254, 100, 0, 255) });            
        squareMesh.setIndices(new short[] { 0, 1, 2, 3});
        squareMesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }
    

    @Override
    public void show() {   
        game.manager.load("data/backdrop.atlas", TextureAtlas.class);
        game.manager.load("data/loading.atlas", TextureAtlas.class);
        game.manager.finishLoading();
        stage = new Stage();
        
        TextureAtlas bgatlas = game.manager.get("data/backdrop.atlas", TextureAtlas.class);
        screenBg = new Image(bgatlas.findRegion("loading"));  
        stage.addActor(screenBg);
        
        TextureAtlas loadingAsset = game.manager.get("data/loading.atlas", TextureAtlas.class);
        LoadingQuote = new Image(loadingAsset.findRegion("aap_quote")); 
        aapLogo = new Image(loadingAsset.findRegion("aap_logo")); 
        ggLogo = new Image(loadingAsset.findRegion("gg_logo"));
        stage.addActor(LoadingQuote);
        stage.addActor(aapLogo);
        stage.addActor(ggLogo);
        
        // Add everything to be loaded, for instance:
        if(this.ScreenCode == Constant.SCREEN_MENU){
        	AssetsCommon.load(game.manager);
        }else if(this.ScreenCode == Constant.SCREEN_LEVEL1){
        	AssetsLevel1.load(game.manager);
        }else if(this.ScreenCode == Constant.SCREEN_LEVEL2){
        	AssetsLevel2.load(game.manager);
        }else if(this.ScreenCode == Constant.SCREEN_COMIC1){
        	AssetsComic1.load(game.manager);
        }else if(this.ScreenCode == Constant.SCREEN_COMIC2){
        	AssetsComic2.load(game.manager);
        }if(this.ScreenCode == Constant.SCREEN_INSTRUCTION){
        	AssetsComicAll.load(game.manager);
        }
        
        init();
        game.analyticsEngine.sendView("loadingScreen");
    }

	@Override
	public void init() {
	    showFBdialog = false;
	    isLoaded = false;
		Gdx.input.setInputProcessor(null);	      
        Gdx.input.setCatchBackKey(true);
	}
    
    @Override
    public void resize(int width, int height) {
        // Set our screen to always be 960 x XXX in size
        height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
        width = (int) RunnerGame.VIRTUAL_WIDTH;
        stage.setViewport(width , height, true);
        
        float asset_height = LoadingQuote.getHeight() + aapLogo.getHeight() + ggLogo.getHeight();
        LoadingQuote.setPosition((width-LoadingQuote.getWidth())/2, (height-asset_height)/2+ggLogo.getHeight()+aapLogo.getHeight());
        aapLogo.setPosition((width-aapLogo.getWidth())/2, (height-asset_height)/2+ggLogo.getHeight());
        ggLogo.setPosition((width-ggLogo.getWidth())/2, (height-asset_height)/2);
    }
    
    private void setScreen(){
    	if(this.ScreenCode == Constant.SCREEN_MENU){
    		AssetsCommon.init();
    		this.game.setScreen(new MenuScreen(this.game));
    	}else if(this.ScreenCode == Constant.SCREEN_LEVEL1){
        	AssetsLevel1.init();
    		this.game.setScreen(new GameScreen(this.game,1));
        }else if(this.ScreenCode == Constant.SCREEN_LEVEL2){
        	AssetsLevel2.init();
    		this.game.setScreen(new GameScreen(this.game,2));
        }else if(this.ScreenCode == Constant.SCREEN_COMIC1){
        	AssetsComic1.init();
    		this.game.setScreen(new ComicScreen(this.game,1));
        }else if(this.ScreenCode == Constant.SCREEN_COMIC2){
        	AssetsComic2.init();
    		this.game.setScreen(new ComicScreen(this.game,2));
        }else if(this.ScreenCode == Constant.SCREEN_INSTRUCTION){
        	AssetsComicAll.init();
    		this.game.setScreen(new InstructionScreen(this.game));
        }
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);        
        if(percent>0.01 && showFBdialog == false && 
        		this.ScreenCode == Constant.SCREEN_MENU){
            showFBdialog = true;
            RunnerGame.facebook.login();
        }
        if ((game.manager.update() && percent > 0.99f)) {
        	setScreen();
        }        
        
        stage.act();
        stage.draw();
        
        if(this.isLoaded == false){
            // Interpolate the percentage to make it more smooth
            percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);
            drawBar(RunnerGame.VIRTUAL_WIDTH*percent,45);    
        }
    }

    @Override
    public void hide() {
        // Dispose the loading assets as we no longer need them
    }

	@Override
	public void pause() {
		// TODO Auto-generated method stub			
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub			
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub			
	}


}
