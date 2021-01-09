import java.util.HashMap;
import java.util.Random;

public class Board {
    Cell[][] cells;
    Random random;

    Board(Random random, int size) {
        this.cells = new Cell[size][size];
        this.random = random;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.cells[i][j] = new Cell();
            }
        }
        System.out.println("Создана доска размером " + size + "х" + size);
    }

    void setSymbol(int x, int y, Symbol symbol) {
        cells[x][y].setSymbol(symbol);
    }

/*    boolean isWinningRow(int x, Symbol symbol) {
        int sum = 0;
        for (int i = 0; i < cells[x].length; i++) {
            if (cells[x][i].getSymbol().equals(symbol)) {
                sum++;
            }
        }
        return sum == cells[x].length - 1;
    }*/

    int findWinningCellInRow(int x, Symbol symbol) {
        int y = -1;
        int sum = 0;
        for (int i = 0; i < cells[x].length; i++) {
            if (cells[x][i].getSymbol().equals(symbol)) {
                sum++;
            } else if (cells[x][i].getSymbol().equals(Symbol.EMPTY)) {
                y = i;
            }
        }
        return sum == cells[x].length - 1 ? y : -1;
    }

/*    boolean isWinningCol(int y, Symbol symbol) {
        int sum = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][y].getSymbol().equals(symbol)) {
                sum++;
            }
        }
        return sum == cells.length - 1;
    }*/

    int findWinningCellInCol(int y, Symbol symbol) {
        int x = -1;
        int sum = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][y].getSymbol().equals(symbol)) {
                sum++;
            } else if (cells[i][y].getSymbol().equals(Symbol.EMPTY)) {
                x = i;
            }
        }
        return sum == cells.length - 1 ? x : -1;
    }

    int findWinningCellInLDiag(Symbol symbol) {
        int sum = 0;
        int coord = -1;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][i].getSymbol().equals(symbol)) {
                sum++;
            } else if (cells[i][i].getSymbol().equals(Symbol.EMPTY)) {
                coord = i;
            }
        }
        return sum == cells.length - 1 ? coord : -1;
    }

    int findWinningCellInRDiag(Symbol symbol) {
        int sum = 0;
        int coord = -1;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][cells.length - 1 - i].getSymbol().equals(symbol)) {
                sum++;
            } else if (cells[i][cells.length - 1 - i].getSymbol().equals(Symbol.EMPTY)) {
                coord = i;
            }
        }
        return sum == cells.length - 1 ? coord : -1;
    }

    boolean isWinningRDiag(Symbol symbol) {
        int sum = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][cells.length - 1 - i].getSymbol().equals(symbol)) {
                sum++;
            }
        }
        return sum == cells.length - 1;
    }

    int findEmptyInRow(int rowNum) {
        int colNum = -1;
        for (int i = 0; i < cells.length; i++) {
            if (isEmptyCell(rowNum, i)) {
                colNum = i;
            }
        }
        return colNum;
    }

    int findEmptyInCol(int colNum) {
        int rowNum = -1;
        for (int i = 0; i < cells.length; i++) {
            if (isEmptyCell(i, colNum)) {
                rowNum = i;
            }
        }
        return rowNum;
    }

    int findEmptyInLDiag() {
        int coord = -1;
        for (int i = 0; i < cells.length; i++) {
            if (isEmptyCell(i, i)) {
                coord = i;
            }
        }
        return coord;
    }

    int findEmptyInRDiag() {
        int rowNum = -1;
        for (int i = 0; i < cells.length; i++) {
            if (isEmptyCell(i, cells.length - 1 - i)) {
                rowNum = i;
            }
        }
        return rowNum;
    }

    int findPlaceForOInRow(int rowNum) {
        //TODO метод всегда ищет куда поставить нолик, надо расширить, чтобы искал куда поставить и крестик
        int pos = -1; //Позиция нолика
        int stepX = -1; //Куда поставить нолик
        for (int i = 0; i < cells.length; i++) {
            if (cells[rowNum][i].getSymbol().equals(Symbol.O)) {
                pos = i;
            } else if (cells[rowNum][i].getSymbol().equals(Symbol.X)) {
                System.out.println("В строке №" + rowNum + " не может быть выигрыша");
                pos = -1;
                break;
            }
        }
        if (pos == 0) {
            stepX = cells.length - 1;
        } else if (pos == cells.length - 1) {
            stepX = 0;
        } else if (pos == -1) {
            stepX = -1;
        } else {
            stepX = random.nextBoolean() ? 0 : cells.length - 1;
        }
        return stepX;
    }

    int findPlaceForOInCol(int colNum) {
        //TODO метод всегда ищет куда поставить нолик, надо расширить, чтобы искал куда поставить и крестик
        int pos = -1; //Позиция нолика
        int stepY = -1; //Куда поставить нолик
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][colNum].getSymbol().equals(Symbol.O)) {
                pos = i;
            } else if (cells[i][colNum].getSymbol().equals(Symbol.X)) {
                System.out.println("В столбце №" + colNum + " не может быть выигрыша");
                pos = -1;
                break;
            }
        }
        if (pos == 0) {
            stepY = cells.length - 1;
        } else if (pos == cells.length - 1) {
            stepY = 0;
        } else if (pos == -1) {
            stepY = -1;
        } else {
            stepY = random.nextBoolean() ? 0 : cells.length - 1;
        }
        return stepY;
    }

    int findPlaceForOInLDiag() {
        int coord = -1;
        int pos = -1;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][i].getSymbol().equals(Symbol.O)) {
                pos = i;
            } else if (cells[i][i].getSymbol().equals(Symbol.X)) {
                System.out.println("В левой диагонали не может быть выигрыша");
                pos = -1;
                break;
            }
        }
        if (pos == 0) {
            coord = cells.length - 1;
        } else if (pos == cells.length - 1) {
            coord = 0;
        } else if (pos == -1) {
            coord = -1;
        } else {
            coord = random.nextBoolean() ? 0 : cells.length - 1;
        }
        return coord;
    }

    int findPlaceForOInRDiag() {
        int coord = -1;
        int pos = -1;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][cells.length - 1 - i].getSymbol().equals(Symbol.O)) {
                pos = i;
            } else if (cells[i][cells.length - 1 - i].getSymbol().equals(Symbol.X)) {
                System.out.println("В правой диагонали не может быть выигрыша");
                pos = -1;
                break;
            }
        }
        if (pos == 0) {
            coord = cells.length - 1;
        } else if (pos == cells.length - 1) {
            coord = 0;
        } else if (pos == -1) {
            coord = -1;
        } else {
            coord = random.nextBoolean() ? 0 : cells.length - 1;
        }
        return coord;
    }

    HashMap<String, Integer> getRandomFree() {
        HashMap<String, Integer> coords = new HashMap<>();

        boolean isFound = true;
        while (isFound) {
            Integer x = random.nextInt(3);
            Integer y = random.nextInt(3);
            if (isEmptyCell(x, y)) {
                isFound = false;
                coords.put("x", x);
                coords.put("y", y);
                System.out.println("Случайные координаты найдены: x = " + x + ", y = " + y);
            }
        }
        return coords;
    }

    Cell[][] getCells() {
        return this.cells;
    }

    boolean isEmptyCell(int x, int y) {
        return cells[x][y].getSymbol().equals(Symbol.EMPTY);
    }

    boolean isFullBoard() {
        boolean isFull = true;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].getSymbol().equals(Symbol.EMPTY)) {
                    isFull = false;
                    break;
                }
            }
        }
        return isFull;
    }

    boolean isWinnerRow(int rowNum, Symbol symbol) {
        int sum = 0;
        boolean isWinner = false;
        for (int i = 0; i < cells.length; i++) {
            if (cells[rowNum][i].getSymbol().equals(symbol)) {
                sum++;
            }
        }
        if (sum == cells.length) {
            isWinner = true;
        }
        return isWinner;
    }

    boolean isWinnerCol(int colNum, Symbol symbol) {
        int sum = 0;
        boolean isWinner = false;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][colNum].getSymbol().equals(symbol)) {
                sum++;
            }
        }
        if (sum == cells.length) {
            isWinner = true;
        }
        return isWinner;
    }

    boolean isWinnerLDiag(Symbol symbol) {
        int sum = 0;
        boolean isWinner = false;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][i].getSymbol().equals(symbol)) {
                sum++;
            }
        }
        if (sum == cells.length) {
            isWinner = true;
        }
        return isWinner;
    }

    boolean isWinnerRDiag(Symbol symbol) {
        int sum = 0;
        boolean isWinner = false;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][cells.length - 1 - i].getSymbol().equals(symbol)) {
                sum++;
            }
        }
        if (sum == cells.length) {
            isWinner = true;
        }
        return isWinner;
    }

    void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.cells[i][j].setSymbol(Symbol.EMPTY);
            }
        }
    }
}
