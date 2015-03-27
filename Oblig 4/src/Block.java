import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//The class for the blocks used in Breakout

public class Block extends Rectangle implements Constants{
	
	public Block(double x, double y) {
		this.setHeight(BLOCK_HEIGHT);
		this.setWidth(BLOCK_WIDTH);
		this.setX(x);
		this.setY(y);
		this.setStroke(Color.WHITE);
		this.setStrokeWidth(0.7);
	}
	
	//Calculates at which side the ball hits the block and sends it back
	public boolean ballHit(Ball ball) {
		double x = ball.getCenterX();
		double y = ball.getCenterY();
		
		if(x >= this.getX() && x <= this.getX() + BLOCK_WIDTH) {
			ball.flipY();
			return true;
		} else if(y >= this.getY() && y <= this.getY() + BLOCK_HEIGHT) {
			ball.flipX();
			return true;
		}
		return false;
	}
}
