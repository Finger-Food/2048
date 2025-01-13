import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
		
		if (args.length > 1) {
			System.out.println("Invalid number of arguments. Please provide one argument for size or none for default settings.");
			return;
		}
		
		int size = 4;
		if (args.length == 1)
			try {
				size = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("Invalid argument, must be an int.");
				return;
			}

        Scanner stdin = new Scanner(System.in);
		GameBoard board = build(size);
		board.spawnRandom();
		board.spawnRandom();
		
		Movement direction = null;
		boolean first2048 = true;

		board.drawBoard();
		while (!board.isGameOver()) {			
			direction = Movement.getDirection(stdin.nextLine());
			if (direction == null)
				System.out.println("Invalid command. Please use \"w\", \"a\", \"s\" and \"d\" for up, left, down, and right. Or \"exit\" to quit.");
			
			else if (direction == Movement.EXIT)
				break;
			
			else
				if (!direction.move(board))
					System.out.println("Invalid move. No tiles can move that direction. Try again.");

				else
					board.spawnRandom();

			System.out.println();
			if (board.reached2048() && first2048) {
				System.out.println("`Congrats, now you can keep playing for a higher score.`");
				first2048 = false;
			}
			board.drawBoard();
		}

		if (direction != Movement.EXIT)
			System.out.println("Game over");
    }

	public static GameBoard build(int size) {
		GameBoard board = new Board(size);
		return board;
	}
}

enum Movement {
	UP,
	DOWN,
	RIGHT,
	LEFT,
	EXIT;

	public boolean move(GameBoard board) {
		if (board == null)
			return false;
		
		switch (this) {
			case UP:
				return board.moveUp();
			case DOWN:
				return board.moveDown();
			case RIGHT:
				return board.moveRight();
			case LEFT:
				return board.moveLeft();
			default:
				return false;
		}
	}

	public static Movement getDirection(String input) {
		if (input.equals("exit"))
			return EXIT;
		
		if (input.length() != 1)
			return null;
		
		switch (input.toLowerCase().charAt(0)) {
			case 'w':
				return UP;
			case 's':
				return DOWN;
			case 'd':
				return RIGHT;
			case 'a':
				return LEFT;
			default:
				return null;
		}
	}
}