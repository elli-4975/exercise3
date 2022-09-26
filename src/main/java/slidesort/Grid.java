package slidesort;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;

public class Grid {
    private int[][] _grid;
    private int[][] _OGgrid;
    private int moveIndex;

    /**
     * Create a new grid
     *
     * @param seedArray is not null
     *                  and seedArray.length > 0
     *                  and seedArray[0].length > 0
     */
    public Grid(int[][] seedArray) {
        int rows = seedArray.length;
        int cols = seedArray[0].length;
        _grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                _grid[i][j] = seedArray[i][j];
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Grid) {
            Grid g2 = (Grid) other;
            if (this._grid.length != g2._grid.length) {
                return false;
            }
            if (this._grid[0].length != g2._grid[0].length) {
                return false;
            }
            int rows = _grid.length;
            int cols = _grid[0].length;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (this._grid[i][j] != g2._grid[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if this grid is a valid grid.
     * A grid is valid if, for c = min(rows, cols),
     * the grid contains zero or more values in [1, c]
     * exactly once and all other entries are 0s.
     *
     * @return true if this is a valid grid and false otherwise
     */
    public boolean isValid() {
        // TODO: implement this method
        //make a new array with non-zero elements
        boolean valid = true;
        int rows = _grid.length;
        int cols = _grid[0].length;
        int items = rows * cols;
        int numZeros = 0;
        ArrayList<Integer> noZeros = new ArrayList<Integer>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (_grid[i][j] != 0) {
                    noZeros.add(_grid[i][j]);
                } else {
                    numZeros++;
                }
            }
        }
        if (numZeros == items) {
            return true;
        }
        //System.out.println(valid);
        int c = Collections.max(noZeros);
        for (int x = 1; x < c + 1; x++) {
            if (!noZeros.contains(x)) {
                valid = false;
            } else if (noZeros.indexOf(x) != noZeros.lastIndexOf(x)) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Check if this grid is sorted.
     * A grid is sorted iff it is valid and,
     * for all pairs of entries (x, y)
     * such that x > 0 and y > 0,
     * if x < y then the position(x) < position(y).
     * If x is at location (i, j) in the grid
     * then position(x) = i * (number of cols) + j.
     *
     * @return true if the grid is sorted and false otherwise.
     */
    public boolean isSorted() {
        boolean sorted = true;
        int rows = _grid.length;
        int cols = _grid[0].length;
        int items = rows * cols;
        int numZeros = 0;
        int[] valx = {0, 0, 0};
        int[] valy = {0, 0, 0};
        int posx = 0;
        int posy = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (_grid[i][j] == 0) {
                    numZeros++;
                } else if (_grid[i][j] != 0) {
                    valx = valy;
                    valy = new int[]{i, j, _grid[i][j]};
                    if (valx[2] < valy[2]) {
                        sorted = true;
                    } else {
                        return false;
                    }
                }
            }
        }
        if (numZeros == items) {
            return true;
        }
        return sorted;
    }

    /**
     * Check if a list of moves is feasible.
     * A move is feasible if it starts with a non-zero entry,
     * does not move that number off the grid,
     * and it does not involve jumping over another non-zero number.
     *
     * @param moveList is not null.
     * @return true if the list of moves are all feasible
     * and false otherwise.
     * By definition an empty list is always feasible.
     */
    public boolean validMoves(List<Move> moveList) {
        // TODO: implement this method
        boolean works = true;
        int rows = _grid.length;
        int cols = _grid[0].length;
        int[][] gridTest = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gridTest[i][j] = _grid[i][j];
            }
        }
        //check whether it is column or row movement
        //check max up, down, left, right movement
        //compare displcement with max
        //if true, do move applyMoves
        /*public Move(Position startingPosition, boolean rowMove, int displacement) {
            this.startingPosition = startingPosition;
            this.rowMove = rowMove;
            this.displacement = displacement;
        }
        i, j*/
        //preliminary tests
        for (int list = 0; list < moveList.size(); list++) {
            Move move = moveList.get(moveIndex);
            if (gridTest[move.startingPosition.i][move.startingPosition.j] == 0) {
                return false;
            } else if (move.rowMove == true) {
                if (move.displacement + move.startingPosition.j > gridTest.length - 1 || move.displacement + move.startingPosition.j < 0) {
                    return false;
                }
            } else if (move.rowMove == false) {
                if (move.displacement + move.startingPosition.i > gridTest[0].length - 1 || move.displacement + move.startingPosition.i < 0) {
                    return false;
                }
            } else {
                works = true;
            }
        }


        for (moveIndex = 0; moveIndex < moveList.size(); moveIndex++) {
            Move move = moveList.get(moveIndex);
            if (move.rowMove == true) {
                if (move.displacement > 0) {
                    for (int j = 1; j < move.displacement + move.startingPosition.i; j++) {
                        if (gridTest[move.startingPosition.i][move.startingPosition.j + j] != 0) {
                            return false;
                        } else {
                            works = true;
                        }
                    }
                } else if (move.displacement < 0) {
                    for (int j = -1; j > move.displacement; j--) {
                        if (gridTest[move.startingPosition.i][move.startingPosition.j + j] != 0) {
                            return false;
                        } else {
                            works = true;
                        }
                    }
                }

            } else if (move.rowMove == false) {
                //j value
                //positive is down
                if (move.displacement > 0) {
                    for (int i = 1; i < move.displacement; i++) {
                        if (gridTest[move.startingPosition.i + i][move.startingPosition.j] != 0) {
                            return false;
                        } else {
                            works = true;
                        }
                    }
                } else if (move.displacement < 0) {
                    for (int i = -1; i > move.displacement; i--) {
                        if (gridTest[move.startingPosition.i + i][move.startingPosition.j] != 0) {
                            return false;
                        } else {
                            works = true;
                        }
                    }
                }
            }
            if (works == true) {
                int movingVal = gridTest[move.startingPosition.i][move.startingPosition.j];
                if (move.rowMove == true) {
                    gridTest[move.startingPosition.i][move.startingPosition.j + move.displacement] = movingVal;
                    gridTest[move.startingPosition.i][move.startingPosition.j] = 0;
                } else if (move.rowMove == false) {
                    gridTest[move.startingPosition.i + move.displacement][move.startingPosition.j] = movingVal;
                    gridTest[move.startingPosition.i][move.startingPosition.j] = 0;
                }
            } else {
                return false;
            }
        }
        return works;
    }


    /**
     * Apply the moves in moveList to this grid
     *
     * @param moveList is a valid list of moves
     */
    public void applyMoves(List<Move> moveList) {
        int rows = _grid.length;
        int cols = _grid[0].length;
        int[][] gridMove = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gridMove[i][j] = _grid[i][j];
            }
        }
        for (moveIndex = 0; moveIndex < moveList.size(); moveIndex++) {
            Move moving = moveList.get(moveIndex);
            int movingVal = gridMove[moving.startingPosition.i][moving.startingPosition.j];
            if (moving.rowMove == true) {
                gridMove[moving.startingPosition.i][moving.startingPosition.j + moving.displacement] = movingVal;
                gridMove[moving.startingPosition.i][moving.startingPosition.j] = 0;
            } else if (moving.rowMove == false) {
                gridMove[moving.startingPosition.i + moving.displacement][moving.startingPosition.j] = movingVal;
                gridMove[moving.startingPosition.i][moving.startingPosition.j] = 0;
            }
        }
        _grid = gridMove;
    }

    /**
     * Return a list of moves that, when applied, would convert this grid
     * to be sorted
     *
     * @return a list of moves that would sort this grid
     */
    public List<Move> getSortingMoves() {
        // TODO: implement this method
        int rows = _grid.length;
        int cols = _grid[0].length;
        //first step is to move all of the integers together so there are no zeros btwn them
        //then see if there are the same number or more rows as non-zero elements
        //then move the element to the corresponding row
        return null; // TODO: change this
    }
}
