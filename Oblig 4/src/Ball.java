import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

//The class used for the ball in Breakout

public class Ball extends Circle implements Constants {
	//How far the ball should move at next update
	private double changeX;
	private double changeY;
	
	public Ball() {
		changeX = 1;
		changeY = -1;
		this.setRadius(BALL_RADIUS);
		this.centerBall();
		this.setFill(Color.WHITE);
	}
	
	public void setX(double x) { changeX = x; }
	public double getX() { return changeX; }
	
	public void setY(double y) { changeY = y; }
	public double getY() { return changeY; }
	
	public void flipX() { changeX *= -1; }
	public void flipY() { changeY *= -1; }
	
	public void centerBall() {
		this.setCenterX(GAME_WIDTH / 2);
		this.setCenterY(GAME_HEIGHT / 1.5);
	}
}
