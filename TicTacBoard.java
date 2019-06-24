/**
 * 
 * The board itself.  
 * It's perfectly fine without this as separate class.  
 * Just thought intuitively this should be a different class.
 */
public class TicTacBoard {
	
	private final Cell[][] mBoard;
	private final int mSize;
	
	public enum Cell {
		CROSS, NOUGHT, EMPTY
	}
	
	public TicTacBoard(int size) {
		mSize = size;
		mBoard = new Cell[size][size];
		init();
	}
	
	private void init() {
		for (int i = 0; i < mSize; i++) {
			for (int j = 0; j < mSize; j++) {
				mBoard[i][j] = Cell.EMPTY;
			}
		}
	}

	public Cell getCell(int row, int col) {
		try {
			return mBoard[row][col];			
		} catch (Exception e) {
			return null;
		}
	}
	
	public int getSize() {
		return mSize;
	}
	
	public int draw(int row, int col, Cell mark){
		if (mBoard[row - 1][col - 1] == Cell.EMPTY) {
			mBoard[row - 1][col - 1] = mark;
			return 0;
		} else {
			throw new IllegalStateException("Cannot draw on an unempty cell");
		}
	}
	
	/**
	 * TODO not used yet
	 */
	public Cell getVictor() {
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void clean() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
