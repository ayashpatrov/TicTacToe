import javax.swing.*;
import java.util.HashMap;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class Controller {
    Board board;
    int stepX = -1;
    int stepY = -1;
    boolean isDecisionFound = false;
    String message = "";

    Controller() {
        this.board = new Board(3);
    }

    void setSymbol(int x, int y, Symbol symbol) {
        board.setSymbol(x, y, symbol);
    }

    Board getBoard() {
        return this.board;
    }

    void getDecision() throws Exception {
        //Обнуление предыдущего решения
        isDecisionFound = false;
        stepX = -1;
        stepY = -1;
        if (board.isFullBoard()) {
            System.out.println("Игра окончена");
        } else if (findWinner(Symbol.X)) {
            System.out.println("Выйграли крестики");
        } else if (findWinner(Symbol.O)) {
            System.out.println("Выйграли нолики");
        } else {
            System.out.println("Ищем почти выйгравших ноликов, чтобы завершить игру");
            findPlace(Symbol.O);

            System.out.println("Ищем почти выйгравших крестиков, чтобы заблокировать их");
            findPlace(Symbol.X);

            System.out.println("Ищем место, где есть потенциал выстроить три нолика");
            findPotentialLine();
        }

        if (stepX != -1 && stepY != -1) {
            System.out.println("Принято решение x = " + stepX + ", y = " + stepY);
            board.setSymbol(stepX, stepY, Symbol.O);
        } else {
            //TODO Обработать ошибку
            System.out.println("Решение не найдено");

        }
        if (findWinner(Symbol.X)) {
            System.out.println("Выйграли крестики");
            message = "Выйграли крестики";
        } else if (findWinner(Symbol.O)) {
            System.out.println("Выйграли нолики");
            message = "Выйграли нолики";
            Object[] options = {"Начать заново",
                    "Выйти из игры"};


            int n = JOptionPane.showConfirmDialog(
                    null,
                    message + ", начать заново?",
                    "Игра окончена",
                    JOptionPane.YES_NO_OPTION);
            System.out.println("option n = " + n);
            if (n == 0) {
                board.reset();
            } else {
                //close();
            }
        }
    }

    void findPlace(Symbol symbol) {
        //Смотрим надо ли блокировать ход игрка, если находим, сразу блокируем
        //По горизонтали
        if (!isDecisionFound) {
            for (int i = 0; i < board.getCells().length; i++) {
                if (board.isWinningRow(i, symbol)) {
                    stepX = i;
                    if (board.findEmptyInRow(i) != -1) {
                        stepY = board.findEmptyInRow(i);
                        isDecisionFound = true;
                        System.out.println("Решение найдено в блоке поиска горизонталей из " + Symbol.getDescr(symbol));
                        break;
                    } else {
                        //TODO Обработать ошибку
                        stepX = -1;
                        System.out.println("В строке №" + i + " нет пустой ячейки");
                    }
                }
            }
        }
        //По вертикали
        if (!isDecisionFound) {
            for (int i = 0; i < board.getCells().length; i++) {
                if (board.isWinningCol(i, symbol)) {
                    if (board.findEmptyInCol(i) != -1) {
                        stepX = board.findEmptyInCol(i);
                        stepY = i;
                        isDecisionFound = true;
                        System.out.println("Решение найдено в блоке поиска вертикалей из " + Symbol.getDescr(symbol));
                        System.out.println("x = " + stepX);
                        System.out.println("y = " + stepY);
                        break;
                    } else {
                        //TODO Обработать ошибку
                        System.out.println("В столбце №" + i + " нет пустой ячейки");
                        //throw new RuntimeException("В столбце №" + i + " нет пустой ячейки");
                    }

                }
            }
        }
        //По диагоналям
        if (!isDecisionFound) {
            if (board.isWinningLDiag(symbol)) {
                if (board.findEmptyInLDiag() != -1) {
                    stepX = board.findEmptyInLDiag();
                    stepY = board.findEmptyInLDiag();
                    System.out.println("Решение найдено в блоке поиска левых диагоналей из " + Symbol.getDescr(symbol));
                    isDecisionFound = true;
                }
            }
        }
        if (!isDecisionFound) {
            if (board.isWinningRDiag(symbol)) {
                if (board.findEmptyInRDiag() != -1) {
                    stepX = board.findEmptyInRDiag();
                    stepY = board.cells.length - 1 - board.findEmptyInRDiag();
                    System.out.println("Решение найдено в блоке поиска правых диагоналей из " + Symbol.getDescr(symbol));
                    isDecisionFound = true;
                }
            }
        }
    }

    void findPotentialLine() {
        //Ищем линию по горизонтали, в которой есть хотя бы один нолик, и ни одного крестика
        if (!isDecisionFound) {
            for (int i = 0; i < board.getCells().length; i++) {
                if (board.checkRow(i) != -1) {
                    stepX = i;
                    stepY = board.checkRow(i);
                    isDecisionFound = true;
                    System.out.println("Решение найдено в блоке поиска потенциального горизонтального выйгрыша для ноликов");
                }
            }
        }
        //Ищем линию по вертикали, в которой есть хотя бы один нолик, и ни одного крестика
        if (!isDecisionFound) {
            for (int i = 0; i < board.getCells().length; i++) {
                if (board.checkCol(i) != -1) {
                    stepX = board.checkCol(i);
                    stepY = i;
                    isDecisionFound = true;
                    System.out.println("Решение найдено в блоке поиска потенциального вертикального выйгрыша для ноликов");
                }
            }
        }
        //Ищем линию по диагонали, в которой есть хотя бы один нолик, и ни одного крестика
        if (!isDecisionFound) {
            if (board.checkLDiag() != -1) {
                stepX = board.checkLDiag();
                stepY = board.checkLDiag();
                isDecisionFound = true;
                System.out.println("Решение найдено в блоке поиска потенциальной левой диагонали выйгрыша для ноликов");
            }
        }
        //Ищем линию по диагонали, в которой есть хотя бы один нолик, и ни одного крестика
        if (!isDecisionFound) {
            if (board.checkLDiag() != -1) {
                stepX = board.checkLDiag();
                stepY = board.checkLDiag();
                isDecisionFound = true;
                System.out.println("Решение найдено в блоке поиска потенциальной левой диагонали выйгрыша для ноликов");
            }
        }
        if (!isDecisionFound) {
            HashMap<String, Integer> coord = board.getRandomFree();
            stepX = coord.get("x");
            stepY = coord.get("y");
            isDecisionFound = true;
            System.out.println("Решение найдено случайно");
        }
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
}
