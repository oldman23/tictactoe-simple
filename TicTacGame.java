import java.util.ArrayList;


public class TicTacGame {
	private final TicTacBoard mBoard;
	private final int mVictorySize;
	private final  Object mSynchObject = new Object();
	private final ArrayList<TicTacListener> mListeners= new ArrayList<TicTacListener>();
	
	private State mCurrentState;
	private Player mCurrentTurn;
	private boolean mPaused = false;
    
	public enum State {
		PLAYING, DRAW, CROSS_WON, NOUGHT_WON
	}

	public enum Player {
		CROSS, NOUGHT
	}
	
	/**
	 * @param boardSize  The size of the board.  Because the board is square-shaped (n*n), only a single integer is required.
	 * @param victorySize The size of line required in order to form a victory.  Note that boardSize should be greater than this value.
	 */
	public TicTacGame(int boardSize, int victorySize) {
		if (boardSize < 3 || victorySize < 3) {
			throw new IllegalArgumentException("Either board size or victory size is too small.");
		}
		if (boardSize < victorySize) {
			throw new IllegalArgumentException("Board size is smaller than victory size");
		}
		mBoard = new TicTacBoard(boardSize);
		mCurrentState = State.PLAYING;
		mCurrentTurn  = Player.NOUGHT;
		mVictorySize = victorySize;
	}

	/**
	 * 
	 * Add a listener in an observers pattern.
	 * Events are broadcasted to all the registered listeners.
	 */
	public void addListener(TicTacListener t) {
		mListeners.add(t);
	}
	
	public Player whosTurn() {
		return mCurrentTurn;
	}
	
	/**
	 * @param player The player making the move (circle/cross). Instead of
	 *               automatically alternating between players, you'll need to specify
	 *               who is making the move because it makes more sense in a real
	 *               application. It makes it more robust and resistant to
	 *               mistakes.
	 * @param row    Row coordinate of the move (starts from 1)
	 * @param col    Col coordinate of the move (starts from 1)
	 * @return true if the move is registered, false if the move is not registered
	 */
	public boolean move(Player player, int row, int col) {
		boolean registered = false;
		P: try {
			/**
			 * synchronized to make this atomic.
			 */
			synchronized(mSynchObject) {
				if (!isValidMove(player, row, col)) {
					break P;
				}
				if (player.equals(Player.CROSS)) {
					mBoard.draw(row, col, TicTacBoard.Cell.CROSS);
					mCurrentTurn = Player.NOUGHT;
				} else {
					mBoard.draw(row, col, TicTacBoard.Cell.NOUGHT);
					mCurrentTurn = Player.CROSS;
				}
				registered = true;
				
				for(TicTacListener lis : mListeners) {
					lis.onMoveRegistered(mBoard, row, col);
				}
				
				if (hasWon(row, col)) {
					mCurrentState = player.equals(Player.CROSS)? State.CROSS_WON : State.NOUGHT_WON;
					for(TicTacListener lis : mListeners) {
						lis.onWin(mBoard, player);
					}
				} else if (isDraw()) {
					mCurrentState = State.DRAW;
					for(TicTacListener lis : mListeners) {
						lis.onDraw(mBoard);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return registered;
	}
	
	/**
	 * If all spot is filled, return true.  If not, return false;
	 */
	private boolean isDraw() {
		for (int i = 0; i < mBoard.getSize(); i++) {
			for (int j = 0; j < mBoard.getSize(); j++) {
				if(mBoard.getCell(i, j).equals(TicTacBoard.Cell.EMPTY)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * First, use the method countAll() to determine the greatest line that contains (row, col).  
	 * Then, if that line exceeds mVictorySize, then return true. 
	 * If not, return false. 
	 */
	private boolean hasWon(int row, int col) {
		try {
			int maxCount = countAll(row, col);
			if (maxCount >= mVictorySize) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return false;
	}
	
	/**
	 * Counts the length of the greatest line that contains the coordinate (row, col).
	 * This method uses the recursive method count().
	 * 
	 * this method counts the length of 4 lines :
	 * 	horizontal line (direction - west, east)
	 *  vertical   line (direction - north, south)
	 *  diagonal_1 line (direction - northeast, southwest)
	 *  diagonal_2 line (direction - northwest, southeast)
	 */
	private enum Directions {N, NE, E, SE, S, SW, W, NW}
	private int countAll(int row, int col) {
		int hor = 0, ver = 0, d1 = 0, d2 = 0;
		TicTacBoard.Cell c = mBoard.getCell(row - 1, col- 1);
		
		hor = 1 + count(row, col + 1, c, Directions.E) + count(row, col - 1, c, Directions.W);
		ver = 1 + count(row - 1, col, c, Directions.N) + count(row + 1, col, c, Directions.S);
		d1  = 1 + count(row - 1, col + 1, c, Directions.NE) + count(row + 1, col - 1, c, Directions.SW);
		d2  = 1 + count(row - 1, col - 1, c, Directions.NW) + count(row + 1, col + 1, c, Directions.SE);
		
		int max = Math.max(hor, ver);
		max     = Math.max(max, d1);
		max     = Math.max(max, d2);
		return max;
	}
	
	/**
	 * recursive.
	 * The code speaks for itself.
	 */
	private int count(int row, int col, TicTacBoard.Cell pcel, Directions d) {
		TicTacBoard.Cell c = mBoard.getCell(row - 1, col - 1);
		if (c == null) {
			return 0;
		}
		if (!c.equals(pcel)) {
			return 0;
		}
		int newRow = row;
		int newCol = col;
		switch (d) {
		case N:
			newRow--;
			break;
		case NE:
			newRow--;
			newCol++;
			break;
		case E:
			newCol++;
			break;
		case SE:
			newCol++;
			newRow++;
			break;
		case S:
			newRow++;
			break;
		case SW:
			newRow++;
			newCol--;
			break;
		case W:
			newCol--;
			break;
		case NW:
			newCol--;
			newRow--;
			break;
		default:
			break;
		}
		return 1 + count(newRow, newCol, c, d);
	}
	
	/**
	 * self-explanatory
	 */
	private boolean isValidMove(Player player, int row, int col) {
		String reason = "";
		boolean valid = false;
		P: try {
			if (mPaused == true) {
				reason = "Game is paused";
				break P;
			}
			if (!mCurrentState.equals(State.PLAYING)) {
				reason = "Game is already over";
				break P;
			}
			if (!player.equals(mCurrentTurn)) {
				reason = "Playing out of turn";
				break P;
			}
			if (row < 1 || row > mBoard.getSize()) {
				reason = "Invalid row value";
				break P;
			}
			if (col < 1 || col > mBoard.getSize()) {
				reason = "Invalid column value";
				break P;
			}
			if (!mBoard.getCell(row - 1, col - 1).equals(TicTacBoard.Cell.EMPTY)) {
				reason = "That spot is already filled.";
				break P;
			}
			valid = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!valid) {
			for(TicTacListener lis : mListeners) {
				lis.onMoveInvalid(mBoard, row, col, reason);
			}			
		}
		return valid;
	}
	
	/**
	 * Pause the game, during which no moves are registered.
	 */
	public boolean pause() {
		mPaused = true;
		for(TicTacListener lis : mListeners) {
			lis.onPause(mBoard);
		}
		return true;
	}

	/**
	 * Unpause the game, continuing as normal
	 */
	public boolean resume() {
		mPaused = false;
		for(TicTacListener lis : mListeners) {
			lis.onResume(mBoard);
		}
		return true;
	}

	/**
	 * Restarts the game, with a clean board.
	 */
	public boolean restart() {
		synchronized (mSynchObject) {
			mBoard.clean();
			mCurrentState = State.PLAYING;
			mCurrentTurn  = Player.NOUGHT;
			mPaused = false;			
		}
		for(TicTacListener lis : mListeners) {
			lis.onRestart(mBoard);
		}
		return true;
	}
	
	/**
	 * Do nothing currently.  Just broadcast to listeners.
	 */
	public boolean start() {
		for (TicTacListener lis : mListeners) {
			lis.onStart(mBoard);
		}
		return true;
	}
}
