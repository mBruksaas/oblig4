import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

//The class for the moving platform used in Breakout

public class Platform extends Ellipse implements Constants{
	private double middle;
	
	public Platform() {
		this.setRadiusY(PLATFORM_HEIGHT);
		this.setRadiusX(PLATFORM_WIDTH);
		middle = PLATFORM_WIDTH / 2;
		this.centerPlatform();
		this.setCenterY(BOTTOM);
		this.setFill(Color.WHITE);
		this.setStroke(Color.MEDIUMBLUE);
		this.setStrokeWidth(2);
	}
	
	public void centerPlatform() { this.setCenterX(GAME_WIDTH / 2 - middle); }
	
	//Calculates at what angle the ball should be sent back at
	//if it is hit
	public void platformHit(Ball ball) {
		if(ball.getY() > 0) {
			ball.setX((ball.getCenterX() - (this.getCenterX() + middle)) / PLATFORM_WIDTH);
			ball.setY(Math.abs(ball.getX()) - 2);
		}
	}
}
