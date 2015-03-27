//A simple interface to hold the global constants used in Breakout

public interface Constants {
	public static final int GAME_WIDTH = 960;
	public static final int GAME_HEIGHT = 540;
	public static final double PLATFORM_WIDTH = 35;
	public static final double PLATFORM_HEIGHT = 15; 
	public static final double BALL_RADIUS = 5;
	public static final double BALL_CENTER = 250;
	public static final double SPEED = 0.006;
	
	public static final double BLOCK_WIDTH = GAME_WIDTH / 17;
	public static final double BLOCK_HEIGHT = BLOCK_WIDTH / 2.5;
	public static final int BOTTOM = (int)(GAME_HEIGHT * 0.95);

}
