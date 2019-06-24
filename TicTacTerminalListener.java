
/**
 * 
 * An observer for TicTacGame that outputs events to terminal.
 * A basic implementation.
 * 
 */
public class TicTacTerminalListener implements TicTacListener{

	
	public TicTacTerminalListener() {
		
	}
	@Override
	public void onMoveRegistered(TicTacBoard tb, int row, int col) {
		System.out.println(Utils.DrawBoard(tb));
	}

	@Override
	public void onMoveInvalid(TicTacBoard tb, int row, int col, String reason) {
		System.out.println("Invalid move (" + reason + ")");
	}

	@Override
	public void onWin(TicTacBoard tb, TicTacGame.Player player) {
		switch (player) {
		case CROSS:
			System.out.println("*****CROSS WINS!!*****");
			break;
		case NOUGHT:
			System.out.println("*****NOUGHT WINS!!*****");
			break;
		}
	}

	@Override
	public void onDraw(TicTacBoard tb) {
		System.out.println("BOO! No one wins...");
	}

	@Override
	public void onPause(TicTacBoard tb) {
		System.out.println("Game is paused...");
	}

	@Override
	public void onResume(TicTacBoard tb) {
		System.out.println("Game is resumed...");
	}

	@Override
	public void onRestart(TicTacBoard tb) {
		System.out.println("Game is restarted...");
		System.out.println(Utils.DrawBoard(tb));
	}

	@Override
	public void onStart(TicTacBoard tb) {
		System.out.println(Utils.DrawBoard(tb));
	}
}
