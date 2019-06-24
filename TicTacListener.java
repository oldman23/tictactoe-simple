
/**
 * 
 * Using the observer pattern enables code reuse for many possible usage.
 * Decouples the game itself from the presentation.  
 * 
 * intuitively, a game is observed by (many) observers.
 * 
*/

public interface TicTacListener {
	public void onMoveRegistered(TicTacBoard tb, int row, int col);
	public void onMoveInvalid(TicTacBoard tb, int row, int col, String reason);
	public void onWin(TicTacBoard tb, TicTacGame.Player player);
	public void onDraw(TicTacBoard tb);
	public void onPause(TicTacBoard tb);
	public void onResume(TicTacBoard tb);
	public void onRestart(TicTacBoard tb);
	void onStart(TicTacBoard tb);
}
