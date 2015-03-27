import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A school assignment where the goal was to recreate the old game 'Breakout'.
 * This is the main code used to run the game, the other classes are merely
 * data-holders to make the coding a bit simpler.
 * The goal of the game is to get rid of all the blocks in the top section
 * of the screen, good luck!
 * 
 * @author Martin S. Bruksaas
 */

public class RunBreakout extends Application implements Constants {
	private ArrayList <Block> blocks;
	private Ball ball;
	private int lives = 5;
	private double lvl = 1;
	private Platform bouncer;
	private BorderPane pane;
	private Stage mainStage;
	private StackPane mainPane;
	private Timeline anim;
	
	public void start(Stage primaryStage) {
		//Defining new objects here so that one can retry without re-opening the game
		pane = new BorderPane();
		blocks = new ArrayList<Block>();
		ball = new Ball();
		bouncer = new Platform();
		
		//Creation of everything used for control and instructions
		Button lvl1 = new Button("Level 1");
		Button lvl2 = new Button("Level 2");
		Button lvl3 = new Button("Level 3");
		HBox top = new HBox(lvl1, lvl2, lvl3);
		Rectangle resetRect = new Rectangle(GAME_WIDTH+100, GAME_HEIGHT+100);
		Text resetText = new Text("Click anywhere to start");
		Text playPause = new Text("Press any key to play/pause");
		Text livesLeft = new Text("Lives left: " + lives);
		HBox bottom = new HBox(livesLeft, playPause);
		
		//Using a StackPane as the outer most pane for easy displaying of messages
		mainPane = new StackPane(pane, resetRect, resetText);
		
		//Initializing the 150 blocks, randomizing the color for each layer
		//and randomly deleting 30 blocks
		for(int i=0; i<10; i++) {
			Paint temp = Color.rgb((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
			for(int j=0; j<15; j++) {
				blocks.add(new Block(j * BLOCK_WIDTH + BLOCK_WIDTH, i * BLOCK_HEIGHT + 3 * BLOCK_HEIGHT));
				blocks.get(blocks.size()-1).setFill(temp);
			}
		}
		int maxRandom = 149;
		for(int i=0; i<30; i++) {
			blocks.remove((int)(Math.random() * maxRandom));
			maxRandom--;
		}
		
		for(Block block : blocks)
			pane.getChildren().addAll(block);
		
		pane.getChildren().addAll(bouncer, ball);
		
		//MouseEvent for moving the platform and ActionEvent for the difficulty (levels)
		pane.setOnMouseMoved(e -> { bouncer.setCenterX(e.getX() - PLATFORM_WIDTH / 2); });
		lvl1.setOnAction(e -> { lvl = 1; lives++; reset(); });
		lvl2.setOnAction(e -> { lvl = 2; lives++; reset(); });
		lvl3.setOnAction(e -> { lvl = 3; lives++; reset(); });
		
		//Play/pause functionality whenever a key is pressed, I prefer spacebar
		//but the e.getCode/.getChar/getText does not support the spacebar for
		//some strange reason. However it works when not checking for the e.getCode..
		mainPane.setOnKeyPressed(e -> {
				if(anim.getStatus() == Status.PAUSED) {
					anim.play();
				} else {
					anim.pause();
				}
			});
		
		//Layout settings for everything in the game
		Scene scene = new Scene(mainPane, GAME_WIDTH, GAME_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Breakout");
		pane.setCursor(Cursor.NONE);
		pane.setStyle("-fx-background-color: black;");
		pane.setTop(top);
		pane.setBottom(bottom);
		top.setCursor(Cursor.DEFAULT);
		top.setAlignment(Pos.CENTER);
		bottom.setSpacing(GAME_WIDTH / 2.75);
		resetText.setFont(new Font(80));
		resetText.setFill(Color.BLACK);
		resetText.setStroke(Color.WHITE);
		resetText.setStrokeWidth(2);
		resetRect.setFill(Color.BLACK);
		resetRect.setOpacity(0.5);
		playPause.setFill(Color.WHITE);
		livesLeft.setFill(Color.WHITE);
		mainPane.requestFocus();
		
		anim = new Timeline(new KeyFrame(Duration.seconds(SPEED - lvl * 0.001), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				move();
			}
		}));
		anim.setCycleCount(Timeline.INDEFINITE);
		
		//Removing the big message at the start of each game when clicking
		mainPane.setOnMouseClicked(e -> {
			resetRect.setVisible(false);
			resetText.setVisible(false);
			anim.play();
		});
		
		mainStage = primaryStage;
		
		primaryStage.show();
	}
	
	//The function that gets called by the timer so that the game may progress
	public void move() {
		ball.setCenterX(ball.getCenterX() + ball.getX());
		ball.setCenterY(ball.getCenterY() + ball.getY());
		
		//If the ball is on its way out of the screen, flip its X-dir
		if(ball.getX() < 0) {
			if(ball.getCenterX() - ball.getRadius() < 0)
				 ball.flipX();
		} else {
			if(ball.getCenterX() + ball.getRadius() > GAME_WIDTH + 10)
				ball.flipX();
		}			
		
		//Check for collision with platform and turn the ball around
		if(ball.intersects(bouncer.getBoundsInLocal())) bouncer.platformHit(ball);
		
		//Checking for collision with any of the remaining block, removes
		//the block that might have been hit and checks if you've cleared
		//all the blocks, and thereby have won
		if(ball.getCenterY() < 320) {
			for(Block block : blocks) {
				if(ball.intersects(block.getBoundsInLocal())) {
					if(block.ballHit(ball)) {
						pane.getChildren().remove(block);
						blocks.remove(block);
						if(blocks.isEmpty()){
							anim.stop();
							Text victory = new Text("You've won!");
							victory.setFont(new Font(100));
							victory.setFill(Color.GRAY);
							victory.setStroke(Color.WHITE);
							pane.setCenter(victory);
							pane.setDisable(true);
							mainPane.setDisable(true);
						}
					}
					break;
				}
			}
			//Checks if the ball is on its way out of the top of the screen and
			//checks if you've lost the round
			if(ball.getY() < 0)
				if(ball.getCenterY() - ball.getRadius() < 28)
					ball.flipY();
		} else if(ball.getCenterY() > GAME_HEIGHT) {
			reset();
		}
	}
	
	//The all-so-important reset function. Used to subtract lives, check
	//if it's game over, closing the old stage and relaunching a new one
	public void reset() {
		lives --;
		anim.stop();
		if(lives > 0) {
			mainStage.close();
			start(new Stage());
		} else {
			anim.stop();
			Text defeat = new Text("Game over");
			defeat.setFont(new Font(100));
			defeat.setFill(Color.GRAY);
			defeat.setStroke(Color.WHITE);
			pane.setCenter(defeat);
			pane.setDisable(true);
			mainPane.setDisable(true);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}