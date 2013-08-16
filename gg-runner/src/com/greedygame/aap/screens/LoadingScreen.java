package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.greedygame.AbstractScreen;
import com.greedygame.Assets;
import com.greedygame.aap.RunnerGame;

public class LoadingScreen extends AbstractScreen {

    private Stage stage;
    private Image screenBg;
    private float percent;
    private Mesh squareMesh;
    public LoadingScreen(RunnerGame myGame) {
        super(myGame);
        squareMesh = new Mesh(true, 4, 4, 
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
    }
    
    @Override
    public void show() {    
    	//RunnerGame.LOADING
    	
        // Tell the manager to load assets for the loading screen    
        game.manager.load("data/loading.atlas", TextureAtlas.class);
        
        // Wait until they are finished loading
        game.manager.finishLoading();

        // Initialize the stage where we will place everything
        stage = new Stage();

        // Get our textureatlas from the manager        
        TextureAtlas bgatlas = game.manager.get("data/loading.atlas", TextureAtlas.class);
        
        screenBg = new Image(bgatlas.findRegion("loading"));  

        // Add all the actors to the stage
        stage.addActor(screenBg);

        // Add everything to be loaded, for instance:
        Assets.load(game.manager);        
    }

    @Override
    public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;
        stage.setViewport(width , height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        if (game.manager.update() && percent > 0.99f) { 
            game.setScreen(new MenuScreen(game));
        }

        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);
        stage.act();
        stage.draw();
        drawBar(RunnerGame.VIRTUAL_WIDTH*percent,45);        
    }

    @Override
    public void hide() {
        // Dispose the loading assets as we no longer need them
        game.manager.unload("data/loading.atlas");
    }
    
    public void drawBar(float width, float height){
        squareMesh.setVertices(new float[] {
                0, 0, 0, Color.toFloatBits(128, 0, 0, 255),
                width, 0, 0, Color.toFloatBits(192, 0, 0, 255),
                0, height, 0, Color.toFloatBits(192, 0, 0, 255),
                width, height, 0, Color.toFloatBits(255, 0, 0, 255) });            
        squareMesh.setIndices(new short[] { 0, 1, 2, 3});
        squareMesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }
}
