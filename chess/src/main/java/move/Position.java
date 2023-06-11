package move;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import enums.Direction;
import lombok.Data;

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

    public List<Position> transformDistance(int distance) {
        return List.of(
                transform(Direction.UP, distance),
                transform(Direction.DOWN, distance),
                transform(Direction.LEFT, distance),
                transform(Direction.RIGHT, distance)
        );
    }

    public Position transform(Direction... directions) {
        Position pos = new Position(row, column);
        Arrays.stream(directions).forEach(direction -> {
            movePositionByDirection(pos, direction);
        });
        return pos;
    }

    private void movePositionByDirection(Position pos, Direction direction) {
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

    private Position transform(Direction direction, int repeat) {
        Position pos = new Position(row, column);
        for (int i = 0; i < repeat; i++) {
            movePositionByDirection(pos, direction);
        }
        return pos;
    }

    public static char translateColumnToLetter(int column) {
        return (char) ('a' + column);
    }

    public int getDistanceToPosition(Position position) {
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
