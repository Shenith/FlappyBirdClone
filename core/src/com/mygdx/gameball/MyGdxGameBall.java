package com.mygdx.gameball;

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

import sun.java2d.pipe.SpanShapeRenderer;

public class MyGdxGameBall extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture gameover;
	int flystate = 0;
	float birdy = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 2;
    Texture toptube;
	Texture bottomtube;
	int gap = 500;
	float maxoffset;
	Random randomeGenerator;
	float tubeVelocity = 4;
	int numberoftubes = 4;
    float[] tubex = new float[numberoftubes];
    float[] offset = new float[numberoftubes];
    float distanceBetweenTubes;
    Circle birdCircle;
    //ShapeRenderer shapeRenderer;
    Rectangle[] toppiperect = new Rectangle[numberoftubes];
    Rectangle[] bottompiperect = new Rectangle[numberoftubes];
    int score = 0;
    int scoringtube = 0;
    BitmapFont font;
    int maxScore;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");
        gameover = new Texture("gameover.png");
        maxoffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
        randomeGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth()/2;
        birdCircle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        maxScore = 0;
        startGame();


	}

	public void startGame(){
        birdy = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
        for(int i=0 ; i<numberoftubes ; i++){
            offset[i] = (randomeGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 1200);
            tubex[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            toppiperect[i] = new Rectangle();
            bottompiperect[i] = new Rectangle();
        }
    }

	@Override
	public void render () {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(gameState==1){
            if(tubex[scoringtube] < Gdx.graphics.getWidth()/2){
                score++;
                if(scoringtube<numberoftubes-1){
                    scoringtube++;
                }else {
                    scoringtube = 0;
                }
            }

            if(Gdx.input.justTouched()){
                velocity = -30;

            }
            for(int i=0 ; i<numberoftubes ; i++) {
                if(tubex[i] < - toptube.getWidth() ){
                    tubex[i] += numberoftubes * distanceBetweenTubes;
                    offset[i] = (randomeGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 1200);
                } else {
                    tubex[i] = tubex[i] - tubeVelocity;
                }
                batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + offset[i]);
                batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + offset[i]);

                toppiperect[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + offset[i],toptube.getWidth(),toptube.getHeight());
                bottompiperect[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + offset[i],bottomtube.getWidth(),bottomtube.getHeight());
            }

            if(birdy>0){
                velocity = velocity + gravity;
                birdy -= velocity;
            } else {
                gameState = 2;
            }

        } else if (gameState==0) {
            if(Gdx.input.justTouched()){
                velocity = -50;
                gameState = 1;

            }
        } else if (gameState==2){
            batch.draw(gameover,Gdx.graphics.getWidth()*3/20 - gameover.getWidth() / 2 , Gdx.graphics.getHeight()/2 - gameover.getHeight()/2,Gdx.graphics.getWidth()*8/10,Gdx.graphics.getHeight()*2/10);
            if(Gdx.input.justTouched()){
                if(score>maxScore){
                    maxScore=score;
                }
                velocity = -50;
                scoringtube = 0;
                score = 0;
                gameState = 1;
                startGame();
            }
        }
        if(flystate == 0){
            flystate = 1;
        } else {
            flystate = 0;
        }

        batch.draw(birds[flystate], Gdx.graphics.getWidth()/2 - birds[flystate].getWidth()/2 ,birdy);
        font.draw(batch,String.valueOf(score), 100 , 200);
        font.draw(batch,String.valueOf(maxScore), 100 , 400);
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth()/2 , birdy + birds[flystate].getHeight() / 2,birds[flystate].getWidth() / 2);

        for(int i=0 ; i<numberoftubes ; i++) {
            if(Intersector.overlaps(birdCircle,toppiperect[i])||Intersector.overlaps(birdCircle,bottompiperect[i])){
                gameState = 2;
            }
        }
	}

}
