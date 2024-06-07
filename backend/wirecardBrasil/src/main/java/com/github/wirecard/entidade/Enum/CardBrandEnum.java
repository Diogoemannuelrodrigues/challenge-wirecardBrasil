package com.github.wirecard.entidade.Enum;

public enum CardBrandEnum {
    VISA(1, "Visa"),
    MASTERCARD (2, "Mastercard"),
    AMEX (3, "Amex"),
    DISCOVER(4,"Discover"),
    UNKNOWN(5, "Desconhecido");

    private int cod;
    private String descricao;

    private CardBrandEnum(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static CardBrandEnum toEnum(Integer cod) {

        if (cod == null) {
            return null;
        }

        for (CardBrandEnum x : CardBrandEnum.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + cod);
    }

}
