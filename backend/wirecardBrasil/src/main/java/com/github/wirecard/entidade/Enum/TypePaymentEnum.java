package com.github.wirecard.entidade.Enum;

import com.github.wirecard.exceptions.TypePaymentEnumException;

public enum TypePaymentEnum {

    CREDITO(1, "Crédito"), DEBITO (2, "Débito"), PIX (3, "Pix"), BOLETO (4, "Boleto");

    private int cod;
    private String descricao;

    private TypePaymentEnum(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TypePaymentEnum toEnum(Integer cod) {

        if (cod == null) {
            return null;
        }

        for (TypePaymentEnum x : TypePaymentEnum.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new TypePaymentEnumException(cod);
    }

}

