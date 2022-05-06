public enum TipoCliente {
    PF(0.d),
    PJ(5.d),
    VIP(15.d);

    private final double desconto;

    private TipoCliente(double desconto){
        this.desconto = desconto;
    }

    public double getDesconto() {
        return desconto;
    }
}
