public enum Symbol {
    X("x"),
    O("o"),
    EMPTY("");

    private Symbol(String descr) {
        this.descr = descr;
    }
    private final String descr;

    public static String getDescr(Symbol symbol) {
        return (symbol == null ? null : symbol.descr);
    }

}
