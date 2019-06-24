import java.util.Scanner;


public class Main {
	/**
	 * This plays the game through the standard input/output (terminal).
	 * 
	 * Sorry there's no web version yet. But I did make the classes more modular so
	 * a web version can be implemented rather easily on the java-side.  
	 */
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			TicTacGame ttg = null;
			
			while(true) {
				try {
					System.out.print("Enter the size of the board (e.g.:9) : ");
					int s = Integer.parseInt(sc.nextLine().trim());
					System.out.print("Enter the length of line that would cause a victory (e.g.:5): ");
					int sv = Integer.parseInt(sc.nextLine().trim());
					
					ttg = new TicTacGame(s, sv);
					break;
				} catch (NumberFormatException e1) {
					System.out.println("that is not a number.\n");
				} catch (IllegalArgumentException e2) {
					System.out.println(e2.getMessage());
				} 
			}
			
			ttg.addListener(new TicTacTerminalListener());
			ttg.start();
		
			System.out.println(
					"\nEnter command:  'n <row> <col>' to draw nought at (<row>, <col>),\n\t 'c <row> <col>' to draw cross at (<row>, <col>) \n\t 'pause' to pause the game\n\t 'resume' to resume the game, \n\t 'restart' to restart the game, \n\t 'quit' to quit");
			System.out.println("\n Nought moves first");
			while(true) {
				System.out.print("\nEnter the command : ");
				String i = sc.nextLine().trim().toLowerCase();
				String[] s = i.split(" ");
				if (s[0].equals("restart")) {
					ttg.restart();
				} else if (s[0].equals("pause")) {
					ttg.pause();
				} else if (s[0].equals("resume")) {
					ttg.resume();
				} else if (s[0].equals("quit")) {
					break;
				}  else if (s[0].equals("c") || s[0].equals("n")){
					try {
						TicTacGame.Player p = s[0].equals("c")? TicTacGame.Player.CROSS : TicTacGame.Player.NOUGHT;
						int row = Integer.parseInt(s[1]);
						int col = Integer.parseInt(s[2]);
						ttg.move(p, row, col);
					} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
						System.out.println("Please enter the coordinate correctly.");
					}
				} else {
					System.out.println("Unknown command.");
				}
			}
		}
		
	}
}
