import java.awt.GridLayout;

import javax.swing.*;

public class TicTacToeView implements TicTacToeListener{

	private TicTacToeButton[][] buttons;
	private TicTacToeModel model;
	
	public TicTacToeView() {
		JFrame frame = new JFrame("TicTacToe");
		model = new TicTacToeModel('X');
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuItem resetButton = new JMenuItem("Reset");
		resetButton.addActionListener(e -> {
			model.reset('X');
		});
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(resetButton);
		
		frame.setJMenuBar(menuBar);
		
		JPanel panel = new JPanel(new GridLayout(TicTacToeModel.SIZE, TicTacToeModel.SIZE));
		buttons = new TicTacToeButton[TicTacToeModel.SIZE][TicTacToeModel.SIZE];
		
		for(int y = 0; y < TicTacToeModel.SIZE; y++) {
			for(int x = 0; x < TicTacToeModel.SIZE; x++) {
				TicTacToeButton button = new TicTacToeButton(x, y);
				buttons[x][y] = button;
				button.addActionListener(e -> {
					model.takeTurn(((TicTacToeButton)e.getSource()).getX(), ((TicTacToeButton)e.getSource()).getY());
				});
				panel.add(button);
			}
		}
		frame.add(panel);
		frame.setVisible(true);
		model.addObserver(this);
	}
	
	@Override
	public void handleTicTacToeEvent(TicTacToeEvent e) {
		if(e.getX() == -1 || e.getY() == -1) {
			//Reset board.
			for(int x = 0; x < TicTacToeModel.SIZE; x++) {
				for(int y = 0; y < TicTacToeModel.SIZE; y++) {
					buttons[x][y].setText("");
					buttons[x][y].setEnabled(true);
				}
			}
			JOptionPane.showMessageDialog(null, "Game has been reset.");
		}
		else {
			//Update board.
			buttons[e.getX()][e.getY()].setText(Character.toString(e.getTurn()));
			if(e.getGameState() != TicTacToeEnum.IN_PROGRESS) {
				for(int x = 0; x < TicTacToeModel.SIZE; x++) {
					for(int y = 0; y < TicTacToeModel.SIZE; y++) {
						buttons[x][y].setEnabled(false);
					}
				}
				JOptionPane.showMessageDialog(null, e.getGameState().toString());
			}
		}
	}
	
	public static void main(String[] args) {
		TicTacToeView view = new TicTacToeView();
	}

}
