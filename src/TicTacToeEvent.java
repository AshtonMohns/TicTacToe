import java.util.EventObject;

public class TicTacToeEvent extends EventObject{
	private int x, y;
	private char turn;
	private TicTacToeEnum gameState;
	
	public TicTacToeEvent(TicTacToeModel source, int x, int y, char turn, TicTacToeEnum gameState) {
		super(source);
		this.x = x;
		this.y = y;
		this.turn = turn;
		this.gameState = gameState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public char getTurn() {
		return turn;
	}

	public TicTacToeEnum getGameState() {
		return gameState;
	}
	
	
}
