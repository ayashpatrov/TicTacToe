public class Cell {
    Symbol symbol;

    Cell() {
        this.symbol = Symbol.EMPTY;

        System.out.println("создана ячейка со значением \"" + Symbol.getDescr(symbol) + "\"");
    }

    void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    Symbol getSymbol() {
        return this.symbol;
    }
}