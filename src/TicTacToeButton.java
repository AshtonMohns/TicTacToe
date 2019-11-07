import javax.swing.JButton;

public class TicTacToeButton extends JButton{
	private int x, y;
	
	public TicTacToeButton(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
