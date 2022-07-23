package pointclub.pointclubclient.chess.move;

import java.util.Arrays;

import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pointclub.pointclubclient.chess.enums.Direction;

@Data
@AllArgsConstructor
public class Position {
    private int row;
    private int column;

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
}
