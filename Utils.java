
public class Utils {
	/**
	 * 
	 * This draws the board into a string.  
	 * 
	 */
	public static String DrawBoard(TicTacBoard tb) {
		StringBuilder sb = new StringBuilder();
		int size = tb.getSize();
		
		sb.append("  ");
		for (int i = 0; i < size; i++) {
			sb.append(" ");
			int l = sb.length() + 2;
			sb.append(i + 1);
			if (sb.length() < l) {
				sb.append(" ");
			}
		}
		sb.append("\n");
		
		for (int i = 0; i < size; i++) {
			sb.append("  ");
			for (int j = 0; j < size; j++) {
				sb.append("+--");
			}
			sb.append("+\n");
			int l = sb.length() + 2;
			sb.append(i + 1);
			if (sb.length() < l) {
				sb.append(" ");
			}
			for (int j = 0; j < size; j++) {
				sb.append("| ");
				TicTacBoard.Cell c = tb.getCell(i, j);
				switch (c) {
				case CROSS:
					sb.append("X");
					break;
				case NOUGHT:
					sb.append("O");
					break;
				default:
					sb.append(" ");
					break;
				}
			}
			sb.append("|\n");
		}
		sb.append("  ");
		for (int j = 0; j < size; j++) {
			sb.append("+--");
		}
		sb.append("+\n");
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		TicTacBoard b = new TicTacBoard(15);
		b.draw(2, 5, TicTacBoard.Cell.CROSS);
		b.draw(12, 3, TicTacBoard.Cell.CROSS);
		b.draw(1, 3, TicTacBoard.Cell.NOUGHT);
		b.draw(13, 15, TicTacBoard.Cell.NOUGHT);
		System.out.println(DrawBoard(b));
	}
}
