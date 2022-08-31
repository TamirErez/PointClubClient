package pointclub.pointclubclient.chess.move;

import java.util.Arrays;

import androidx.annotation.NonNull;
import lombok.Data;
import pointclub.pointclubclient.chess.enums.Direction;

@Data
public class Position {
    private int row;
    private int column;
    public static final Position EMPTY_POSITION = new Position(-1, -1);

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
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

    private char translateColumnToLetter(int row) {
        return (char) ('a' + row);
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
