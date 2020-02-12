package com.pathaboi.flabbybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlabbyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, topTube, bottomTube;
	Texture[] birds;
//	ShapeRenderer shapeRenderer ;
	int flapState = 0;
	float birdY= 0;
	float velocity = 0;
	int gameState= 0;
	float gravity = 2;
	float gap = 400;
	float maxTubeOffSet;
	Random randomGenerator;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffSet= new float[numberOfTubes];
	float distanceBetweenTubes;

	Circle birdCircle;
	Rectangle[] topPipeRectangle ;
	Rectangle[] bottomPipeRectangle;

	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffSet = Gdx.graphics.getHeight()/2 - gap/2 -250;

		randomGenerator = new Random();

		birdCircle = new Circle();
//		shapeRenderer= new ShapeRenderer();
		topPipeRectangle = new Rectangle[numberOfTubes];
		bottomPipeRectangle = new Rectangle[numberOfTubes];

		distanceBetweenTubes= Gdx.graphics.getWidth()*3/4;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		startGame();




	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		for(int i =0; i<numberOfTubes; i++){
			tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap- 500);
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + i*distanceBetweenTubes + Gdx.graphics.getWidth();

			topPipeRectangle[i] = new Rectangle();
			bottomPipeRectangle[i] = new Rectangle();
		}

	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState==1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2 ){
				score += 1;

				if (scoringTube<numberOfTubes-1){
					scoringTube++;
				}
				else{
					scoringTube=0;
				}
			}

			if (Gdx.input.justTouched()){
				velocity = -30;

			}
			for(int i =0; i<numberOfTubes; i++){

				if(tubeX[i]< -topTube.getWidth()){
					tubeX[i] += numberOfTubes*distanceBetweenTubes;
					tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap- 500);
				}else{
					tubeX[i]-=tubeVelocity;

				}


				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffSet[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 -bottomTube.getHeight() + tubeOffSet[i]);

				topPipeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffSet[i], topTube.getWidth(), topTube.getWidth());
				bottomPipeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 -bottomTube.getHeight() + tubeOffSet[i], bottomTube.getWidth(), bottomTube.getHeight());


			}
			if(birdY>0) {
				velocity += gravity;
				birdY -= velocity;
			}
			else{
				gameState = 2;
			}
		} else if (gameState == 0){

			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if(gameState == 2){

			if (Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}

		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}



		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

		font.draw(batch,String.valueOf(score),100,200);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for(int i =0; i<numberOfTubes; i++){
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffSet[i], topTube.getWidth(), topTube.getHeight());
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 -bottomTube.getHeight() + tubeOffSet[i], bottomTube.getWidth(), bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle, topPipeRectangle[i])|| Intersector.overlaps(birdCircle, bottomPipeRectangle[i])){
				gameState =2 ;
			}
		}

//		shapeRenderer.end();
		batch.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();

	}
}
