package br.com.letscode.sinquia;

public enum Tipo {
    ALIMENTOS(1.2),
    BEBIDA(2.3),
    HIGIENE(1.5);

    private final double markup;

    Tipo(double markup) {
        this.markup = markup;
    }

    public double getMarkup() {
        return this.markup;
    }
}
