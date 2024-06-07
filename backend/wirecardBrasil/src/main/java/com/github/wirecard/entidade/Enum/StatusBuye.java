package com.github.wirecard.entidade.Enum;

import com.github.wirecard.exceptions.TypePaymentEnumException;

public enum StatusBuye {
    SUCCESS(1, "Success"), FAIL (2, "Fail");

    private int cod;
    private String descricao;

    private StatusBuye(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusBuye toEnum(Integer cod) {

        if (cod == null) {
            return null;
        }

        for (StatusBuye x : StatusBuye.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new TypePaymentEnumException(cod);
    }

}
