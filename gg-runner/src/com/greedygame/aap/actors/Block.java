package com.greedygame.aap.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.greedygame.AbstractActor;
import com.greedygame.BodyData;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerTable;
import com.greedygame.aap.screens.GameScreen;
import com.greedygame.assets.AssetsLevel1;
import com.greedygame.assets.AssetsLevel2;

public class Block extends AbstractActor {

	private Vector2 initialpos = new Vector2();
	private Rectangle margin = new Rectangle();
	private TextureRegion texture;
	private Array<AtlasRegion> textureArray;

	String bodyId = "";
	private int type;
	World world = null;
	Camera camera = null;
	protected Body body;
	private BodyData bodyData;
	private Fixture fixture;
	private int level;
	public Block(RunnerTable table, Vector2 position) {
		this.type = MathUtils.random(0, 3);
		this.initialpos = new Vector2(position.x / GameScreen.SCALE, position.y / GameScreen.SCALE);
		this.world = table.gameScreen.getWorld();
		this.camera = table.gameScreen.getCamera();
		this.level = table.getLevel();
		positionBlock(level);
		createSolidBody();
	}
	
	@Override
	public void init() {
		
	}
	
	void positionBlock(int level){
		margin.x =0;
		margin.y =0;
		margin.width =  0;
		margin.height = 0;
		if(level==1){
			if (type == 1) {
				this.initialpos.y -= 120 / GameScreen.SCALE;
				this.textureArray = AssetsLevel1.Hypocrite;
				this.texture = this.textureArray.get(0);
				this.margin.x = 0 / GameScreen.SCALE;
				this.margin.width = 50 / (2 * GameScreen.SCALE);
				this.margin.y = -40 / GameScreen.SCALE;
				this.type = Constant.BODY_HYPOCRITE;
			} else if (type == 0) {
				this.initialpos.y -= 50 / GameScreen.SCALE;
				this.textureArray = AssetsLevel1.BARRICADE;
				this.texture = this.textureArray.get(0);
	
				this.margin.x = 0 / GameScreen.SCALE;
				this.margin.y = -5 / GameScreen.SCALE;
				this.margin.width = 200 / (2 * GameScreen.SCALE);
	
				this.type = Constant.BODY_POLICE;
			} else if (type == 2) {
				this.initialpos.y += 100 / GameScreen.SCALE;
				this.textureArray = AssetsLevel1.WARING;
				this.texture = this.textureArray.get(0);	
				this.margin.x = 20 / GameScreen.SCALE;
				this.margin.y = 70 / GameScreen.SCALE;
				this.margin.width = 90 / (2 * GameScreen.SCALE);
				this.type = Constant.BODY_WARNING;
				
			} else if (type == 3) {
				this.initialpos.y += 100 / GameScreen.SCALE;
				this.textureArray = AssetsLevel1.WATERGUN;
				this.texture = this.textureArray.get(0);
				this.margin.x = -100 / GameScreen.SCALE;
				this.margin.y = 100/ GameScreen.SCALE;
				this.margin.width = 80 / (2 * GameScreen.SCALE);
				this.type = Constant.BODY_WATERGUN;
			}			
		}else if(level==2){
			if (type == 0) {
				this.initialpos.y -= 150 / GameScreen.SCALE;
				this.textureArray = AssetsLevel2.BUS;
				this.texture = this.textureArray.get(0);
				this.margin.x = -10 / GameScreen.SCALE;
				this.margin.width = 380 / (2 * GameScreen.SCALE);
				this.margin.y = -20 / GameScreen.SCALE;
				this.type = Constant.BODY_BUS;
			} else if (type == 1) {
				this.initialpos.y -= 100 / GameScreen.SCALE;
				this.textureArray = AssetsLevel2.CORRUPT;
				this.texture = this.textureArray.get(0);
	
				this.margin.x = -70 / GameScreen.SCALE;
				this.margin.width = 40 / (2 * GameScreen.SCALE);
	
				this.type = Constant.BODY_COURRPT;
			} else if (type == 2) {
				this.initialpos.y += 70 / GameScreen.SCALE;
				this.textureArray = AssetsLevel2.PILLAR;
				this.texture = this.textureArray.get(0);
				
				this.margin.y = -20 / GameScreen.SCALE;
				this.margin.x = 10 / GameScreen.SCALE;
				this.margin.width = 40 / (2 * GameScreen.SCALE);
				this.type = Constant.BODY_WIRE;
			} else if (type == 3) {
				this.initialpos.y += 90 / GameScreen.SCALE;
				this.textureArray = AssetsLevel2.FLYOVER;
				this.texture = this.textureArray.get(0);
				this.margin.x = -20 / GameScreen.SCALE;
				this.margin.width = 60 / (2 * GameScreen.SCALE);
				this.type = Constant.BODY_FLYOVER;
			}
		}
		
		if(this.margin.height == 0){
			this.margin.height = this.texture.getRegionHeight()/ (2 * GameScreen.SCALE);
		}
		this.setSize(this.texture.getRegionWidth() / GameScreen.SCALE,
		this.texture.getRegionHeight() / GameScreen.SCALE);
		this.setPosition(initialpos.x, initialpos.y);
	}

	public Body getBody() {
		return this.body;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		this.body.setLinearVelocity(RunnerTable.SPEED, 0);	

	}

	public int gettype() {
		return this.type;
	}

	public void setSensor(boolean b) {
		this.fixture.setSensor(b);
	}

	public void onCollisionWith(BodyData a) {
		if (a.getId() == Constant.BODY_MAN) {
			if (this.fixture.isSensor()) {
				this.texture = this.textureArray.get(1);
			}
		}
		
	}

	public void onCollisionEndWith(BodyData a) {

	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);
		if (body.isActive()) {

			float x = body.getPosition().x - getWidth() / 2 - this.margin.x;
			float y = body.getPosition().y - getHeight() / 2 - this.margin.y;
			batch.draw(this.texture, x, y, 10, body.getPosition().y,
					getWidth(), getHeight(), 1, 1, 0);
		}
	
	}
	
	/********
	 * private
	 */
	private void createSolidBody() {
		this.bodyData = new BodyData();
		
		float bodyx = initialpos.x;
		float bodyy = initialpos.y;
		float BodyHalfw = this.margin.width;
		float BodyHalfh = this.margin.height;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(bodyx + BodyHalfw, bodyy + BodyHalfh);

		body = this.world.createBody(bodyDef);
		PolygonShape groundBox = new PolygonShape();
		body = this.world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = groundBox;
		groundBox.setAsBox(BodyHalfw, BodyHalfh);
		fixture = body.createFixture(fixtureDef);
		this.bodyData.setId(this.type);
		this.bodyData.setActor(this);
		this.body.setUserData(this.bodyData);
	}

	public void kill() {
		if(this.bodyData.isKilled() == false){
			this.bodyData.setKilled(true);
			this.body.setUserData(this.bodyData);
			this.body.setActive(false);
		}		
	}

	public Vector2 getBodyPosition() {
		return this.body.getPosition();
	}

}
