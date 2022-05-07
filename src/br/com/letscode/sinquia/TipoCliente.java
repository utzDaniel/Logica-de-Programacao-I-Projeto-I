package br.com.letscode.sinquia;

public enum TipoCliente {
    PF(0.d),
    PJ(0.05d),
    VIP(0.15d);

    private final double desconto;

    TipoCliente(double desconto){
        this.desconto = desconto;
    }

    public double getDesconto() {
        return desconto;
    }
}
