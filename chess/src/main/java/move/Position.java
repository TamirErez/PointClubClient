package move;

import java.util.Arrays;

import androidx.annotation.NonNull;
import lombok.Data;
import enums.Direction;

@Data
public class Position {
    private int row;
    private int column;
    public static final Position EMPTY_POSITION = new Position(-1, -1);

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(String position) {
        column = position.charAt(0) - 'a';
        row = Integer.parseInt(position.substring(1)) - 1;
    }

    public Position transform(Direction... directions) {
        Position pos = new Position(row, column);
        Arrays.stream(directions).forEach(direction -> {
            switch (direction) {
                case UP:
                    pos.up();
                    break;
                case DOWN:
                    pos.down();
                    break;
                case LEFT:
                    pos.left();
                    break;
                case RIGHT:
                    pos.right();
                    break;
            }
        });
        return pos;
    }

    private void up() {
        row++;
    }

    private void down() {
        row--;
    }

    private void left() {
        column--;
    }

    private void right() {
        column++;
    }

    public static char translateColumnToLetter(int column) {
        return (char) ('a' + column);
    }

    public int getDistanceToPosition(Position position){
        return (int) Math.sqrt(
                (position.row - row) * (position.row - row) +
                (position.column - column) * (position.column - column));
    }

    @NonNull
    @Override
    public String toString() {
        return translateColumnToLetter(column) + String.valueOf(row + 1);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Position &&
                (row == ((Position) other).row) &&
                (column == ((Position) other).column);
    }
}