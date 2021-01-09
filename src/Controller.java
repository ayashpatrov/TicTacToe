import java.util.HashMap;
import java.util.Random;

public class Controller {
    class Step {
        int x;
        int y;

        public Step(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    Board board;
    GameFinished gameFinished;

    Controller(Random random, GameFinished gameFinished) {
        this.board = new Board(random, 3);
        this.gameFinished = gameFinished;
    }

    void setSymbol(int x, int y, Symbol symbol) {
        board.setSymbol(x, y, symbol);
    }

    Board getBoard() {
        return this.board;
    }

    void getDecision() {
        if (checkFinished()) return;

        Step step = null;

        if (step == null) {
            System.out.println("Ищем почти выигравших ноликов, чтобы завершить игру");
            step = findPlace(Symbol.O);
        }
        if (step == null) {
            System.out.println("Ищем почти выигравших крестиков, чтобы заблокировать их");
            step = findPlace(Symbol.X);
        }
        if (step == null) {
            System.out.println("Ищем место, где есть потенциал выстроить три нолика");
            step = findPotentialLine();
        }

        System.out.println("Принято решение x = " + step.x + ", y = " + step.y);
        board.setSymbol(step.x, step.y, Symbol.O);

        checkFinished();
    }

    private Step findPlace(Symbol symbol) {
        //Смотрим надо ли блокировать ход игрока, если находим, сразу блокируем
        Step step;

        //По горизонтали
        step = findPlaceInRow(symbol);
        if (step != null) return step;

        //По вертикали
        step = findPlaceInCol(symbol);
        if (step != null) return step;

        //По диагоналям
        step = findPlaceInLDiag(symbol);
        if (step != null) return step;

        step = findPlaceInRDiag(symbol);
        if (step != null) return step;

        return null;
    }

    private Step findPlaceInLDiag(Symbol symbol) {
        int coord = board.findWinningCellInLDiag(symbol);
        if (coord == -1) return null;

        System.out.println("Решение найдено в блоке поиска левых диагоналей из " + Symbol.getDescr(symbol));
        return new Step(coord, coord);
    }

    private Step findPlaceInRDiag(Symbol symbol) {
        int coord = board.findWinningCellInRDiag(symbol);
        if (coord == -1) return null;

        System.out.println("Решение найдено в блоке поиска правых диагоналей из " + Symbol.getDescr(symbol));
        return new Step(coord, board.cells.length - 1 - coord);
    }

    private Step findPlaceInCol(Symbol symbol) {
        for (int i = 0; i < board.getCells().length; i++) {
            int x = board.findWinningCellInCol(i, symbol);
            if (x == -1) continue;

            System.out.println("Решение найдено в блоке поиска вертикалей из " + Symbol.getDescr(symbol));
            return new Step(x, i);
        }
        return null;
    }

    private Step findPlaceInRow(Symbol symbol) {
        for (int i = 0; i < board.getCells().length; i++) {
            int y = board.findWinningCellInRow(i, symbol);
            if (y == -1) continue;

            System.out.println("Решение найдено в блоке поиска горизонталей из " + Symbol.getDescr(symbol));
            return new Step(i, y);
        }
        return null;
    }

    private Step findPotentialLine() {
        Step step;

        //Ищем линию по горизонтали, в которой есть хотя бы один нолик, и ни одного крестика
        step = findPotentialLineInRow();
        if (step != null) return step;


        //Ищем линию по вертикали, в которой есть хотя бы один нолик, и ни одного крестика
        step = findPotentialLineInCol();
        if (step != null) return step;

        //Ищем линию по левой диагонали, в которой есть хотя бы один нолик, и ни одного крестика
        step = findPotentialLineInLDiag();
        if (step != null) return step;

        //Ищем линию по правой диагонали, в которой есть хотя бы один нолик, и ни одного крестика
        step = findPotentialLineInRDiag();
        if (step != null) return step;

        HashMap<String, Integer> coord = board.getRandomFree();
        System.out.println("Решение найдено случайно");

        return new Step(coord.get("x"), coord.get("y"));
    }

    private Step findPotentialLineInLDiag() {
        int coord = board.findPlaceForOInLDiag();
        if (coord == -1) return null;

        System.out.println("Решение найдено в блоке поиска потенциальной левой диагонали выигрыша для ноликов");
        return new Step(coord, coord);
    }

    private Step findPotentialLineInRDiag() {
        int coord = board.findPlaceForOInRDiag();
        if (coord == -1) return null;

        System.out.println("Решение найдено в блоке поиска потенциальной правой диагонали выигрыша для ноликов");
        return new Step(coord, board.cells.length - 1 - coord);
    }

    private Step findPotentialLineInRow() {
        for (int i = 0; i < board.getCells().length; i++) {
            int y = board.findPlaceForOInRow(i);
            if (y == -1) continue;

            System.out.println("Решение найдено в блоке поиска потенциального горизонтального выигрыша для ноликов");
            return new Step(i, y);
        }
        return null;
    }

    private Step findPotentialLineInCol() {
        for (int i = 0; i < board.getCells().length; i++) {
            int x = board.findPlaceForOInCol(i);
            if (x == -1) continue;

            System.out.println("Решение найдено в блоке поиска потенциального вертикального выигрыша для ноликов");
            return new Step(x, i);
        }
        return null;
    }


    boolean findWinner(Symbol symbol) {
        boolean isWinner = false;
        for (int i = 0; i < board.getCells().length; i++) {
            if (board.isWinnerRow(i, symbol)) {
                System.out.println("Победили " + Symbol.getDescr(symbol) + " в строке №" + i);
                isWinner = true;
                break;
            }
        }
        for (int i = 0; i < board.getCells().length; i++) {
            if (board.isWinnerCol(i, symbol)) {
                System.out.println("Победили " + Symbol.getDescr(symbol) + " в столбце №" + i);
                isWinner = true;
                break;
            }
        }
        if (board.isWinnerLDiag(symbol)) {
            System.out.println("Победили " + Symbol.getDescr(symbol) + " в левой диагонали");
            isWinner = true;
        }
        if (board.isWinnerRDiag(symbol)) {
            System.out.println("Победили " + Symbol.getDescr(symbol) + " в правой диагонали");
            isWinner = true;
        }
        return isWinner;
    }

    boolean checkFinished() {
        boolean result = false;

        if (findWinner(Symbol.X)) {
            System.out.println("Выиграли крестики");
            gameFinished.xWin();
            result = true;
        } else if (findWinner(Symbol.O)) {
            System.out.println("Выиграли нолики");
            gameFinished.oWin();
            result = true;
        } else if (board.isFullBoard()) {
            System.out.println("Игра окончена");
            gameFinished.draw();
            result = true;
        }
        return result;
    }
}
