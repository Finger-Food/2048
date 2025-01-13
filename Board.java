import java.util.*;

class InvalidBoardException extends RuntimeException {}

public class Board implements GameBoard {
    private int width;
    private int height;
    private int[][] board;
    private int tileSize;
    private int tiles;
    private boolean reached2048;

    public Board(int size) {
        setSize(size);
        this.board = new int[this.height][this.width];
        this.tiles = 0;
        this.reached2048 = false;
    }

    public void updateTileSize() {
        this.tileSize = (int) Math.log10(Math.pow(2, this.width * this.height + 1)) + 1;
    }

    public int getSize() {
        return this.height;
    }

    public void setSize(int size) {
        if (size <= 1)
            throw new IllegalArgumentException("`size <= 1`");
        this.height = size;
        this.width = size;
        updateTileSize();
    }

    public int[][] getBoard() {
        return this.board;
    }

    public void setBoard(int[][] board) {
        if (board.length != board[0].length)
            throw new InvalidBoardException();

        this.board = board;
        setSize(board.length);

        this.tiles = 0;
        for (int row = 0; row < this.height; row++)
            for (int col = 0; col < this.width; col++) {
                if (this.board[row][col] != 0)
                    this.tiles++;
                if (this.board[row][col] >= 2048)
                    this.reached2048 = true;
            }
    }

    public void spawnRandom() {
        int value, index;
        Random rand = new Random();

        //choose between 2 and 4
        value = (rand.nextInt(2) + 1) * 2;

        //choose between all empty spaces
        index = rand.nextInt(this.width * this.height);
        while (this.board[index/this.width][index%this.width] != 0)
            index = rand.nextInt(this.width * this.height);

        this.board[index/this.width][index%this.width] = value;
        this.tiles++;
    }

    public void drawRow(int[] row) {
        for (int val : row) {
            System.out.print("|");
            for (int i = 0; i < this.tileSize - String.valueOf(val).length(); i++)
                System.out.print(" ");
            
            if (val == 0)
                System.out.print(" ");
            else
                System.out.print(val);
        }
        System.out.println("|");
    }

    public void drawSeparator() {
        for (int i = 0; i < this.width * (this.tileSize + 1) + 1; i++)
            System.out.print("-");
        System.out.println();
    }

    public void drawBoard() {
        for (int[] row : this.board) {
            drawSeparator();
            drawRow(row);
        }
        drawSeparator();
    }

    public boolean isGameOver() {
        if (this.tiles == this.width * this.height) {
            for (int row = 0; row < this.height; row++)
                for (int col = 0; col < this.width; col++)
                    if (row > 0 && this.board[row][col] == this.board[row-1][col])
                        return false;
                    else if (col > 0 && this.board[row][col] == this.board[row][col-1])
                        return false;

            return true;
        }
        return false;
    }

    public boolean reached2048() {
        return this.reached2048;
    }

    public boolean moveUp() {
        boolean moved = false;
        int base;
        int previous = 0;
        int square;

        for (int col = 0; col < this.width; col++) {
            base = 0;
            previous = 0;

            for (int row = 0; row < this.height; row++) {
                square = this.board[row][col];
                if (square == 0)
                    continue;

                // collide square
                if (square == previous) {
                    this.board[base - 1][col] = previous * 2;
                    previous = 0;
                    this.board[row][col] = 0;
                    moved = true;

                    this.reached2048 = this.reached2048 || square == 1024;
                    this.tiles--;
                    continue;
                }

                //move square
                if (row != base) {
                    this.board[base][col] = square;
                    this.board[row][col] = 0;
                    moved = true;
                }

                previous = square;
                base++;
            }
        }
        return moved;
    }

    public boolean moveDown() {
        boolean moved = false;
        int base;
        int previous = 0;
        int square;

        for (int col = 0; col < this.width; col++) {
            base = this.height - 1;
            previous = 0;

            for (int row = this.height - 1; row >= 0; row--) {
                square = this.board[row][col];
                if (square == 0)
                    continue;

                // collide square
                if (square == previous) {
                    this.board[base + 1][col] = previous * 2;
                    previous = 0;
                    this.board[row][col] = 0;
                    moved = true;

                    this.reached2048 = this.reached2048 || square == 1024;
                    this.tiles--;
                    continue;
                }

                //move square
                if (row != base) {
                    this.board[base][col] = square;
                    this.board[row][col] = 0;
                    moved = true;
                }

                previous = square;
                base--;
            }
        }
        return moved;
    }

    public boolean moveRight() {
        boolean moved = false;
        int base;
        int previous = 0;
        int square;
            
        for (int row = 0; row < this.height; row++) {
            base = this.width - 1;
            previous = 0;

            for (int col = this.width - 1; col >= 0; col--) {
                square = this.board[row][col];
                if (square == 0)
                    continue;

                // collide square
                if (square == previous) {
                    this.board[row][base + 1] = previous * 2;
                    previous = 0;
                    this.board[row][col] = 0;
                    moved = true;

                    this.reached2048 = this.reached2048 || square == 1024;
                    this.tiles--;
                    continue;
                }

                //move square
                if (col != base) {
                    this.board[row][base] = square;
                    this.board[row][col] = 0;
                    moved = true;
                }

                previous = square;
                base--;
            }
        }
        return moved;
    }

    public boolean moveLeft() {
        boolean moved = false;
        int base;
        int previous = 0;
        int square;
            
        for (int row = 0; row < this.height; row++) {
            base = 0;
            previous = 0;

            for (int col = 0; col < this.width; col++) {
                square = this.board[row][col];
                if (square == 0)
                    continue;

                // collide square
                if (square == previous) {
                    this.board[row][base - 1] = previous * 2;
                    previous = 0;
                    this.board[row][col] = 0;
                    moved = true;

                    this.reached2048 = this.reached2048 || square == 1024;
                    this.tiles--;
                    continue;
                }

                //move square
                if (col != base) {
                    this.board[row][base] = square;
                    this.board[row][col] = 0;
                    moved = true;
                }

                previous = square;
                base++;
            }
        }
        return moved;
    }
}