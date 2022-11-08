package com.example.pizzaria;

public class Pizza {

    private String sabor;
    private float precoBas, precoFin;
    private float coefTam;
    private char tamanho;
    private int qtd;

    public Pizza(String sabor, float precoBas, float coefTam, char tamanho) {
        this.sabor = sabor;
        this.precoBas = precoBas;
        this.coefTam = coefTam;
        this.tamanho = tamanho;
        this.setPrecoFin();
        this.qtd = 1;
    }

    private void setPrecoFin() {
        float coef = 1f;
        switch(tamanho) {
            case 'P': coef = coefTam; break;
            case 'M': coef = coefTam*coefTam; break;
            case 'G': coef = coefTam*coefTam*coefTam; break;
        }
        precoFin = precoBas*coef*qtd;
    }

    public float getPreco() {
        return precoFin;
    }

    public void setQtd(int qt) {
        this.qtd = qt;
    }

    @Override
    public String toString() {
        return String.format("%s %s: R$%.2f",sabor,tamanho,precoFin);
    }

}
