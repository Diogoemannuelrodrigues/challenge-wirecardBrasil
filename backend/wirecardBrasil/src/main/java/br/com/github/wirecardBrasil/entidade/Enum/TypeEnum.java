package br.com.github.wirecardBrasil.entidade.Enum;

public enum TypeEnum {
    BOLETO(1, "Boleto"), CARTAO_DE_CREDITO(2, "Cartao de Credito");

    private int cod;
    private String descricao;

    private TypeEnum(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TypeEnum toEnum(Integer cod) {

        if (cod == null) {
            return null;
        }

        for (TypeEnum x : TypeEnum.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + cod);
    }

}
