package com.aironbruce.registroscep.otherclasses;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CEP implements Serializable {

    private final String cep;
    private final String logradouro;
    private final String complemento;
    private final String bairro;
    private final String localidade;
    private final String uf;

    public CEP(String cep, String logradouro, String complemento, String bairro, String localidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }

    public String getCep() {return cep;}
    public String getLogradouro() {return logradouro;}
    public String getComplemento() {return complemento;}
    public String getBairro() {return bairro;}
    public String getLocalidade() {return localidade;}
    public String getUf() {return uf;}

    @NonNull
    @Override
    public String toString() {return toString(1,1,1,1,1,1);}

    public String toString(int a, int b, int c, int d, int e, int f) {

        String result = "";
        if (getCep() != null && a == 1) result += (getCep() + ": ");
        if (getLogradouro() != null && b == 1) result += (getLogradouro() + ", ");
        if (getComplemento() != null && c == 1) result += (getComplemento() + ". ");
        if (getBairro() != null && d == 1) result += ("Bairro " + getBairro() + ", ");
        if (getLocalidade() != null && e == 1) result += getLocalidade();
        if (getUf() != null && f == 1) result += (" - " + getUf());

        return result;
    }
}
