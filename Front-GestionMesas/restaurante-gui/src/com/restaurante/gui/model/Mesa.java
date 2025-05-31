package com.restaurante.gui.model;

public class Mesa {
    private Long id;
    private int numero;
    private boolean ocupada;
    private boolean Limpieza;


    public Mesa() {}

    public Mesa(Long id, boolean limpieza, boolean ocupada, int numero) {
        this.id = id;
        Limpieza = limpieza;
        this.ocupada = ocupada;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public boolean isLimpieza() {
        return Limpieza;
    }

    public void setLimpieza(boolean limpieza) {
        Limpieza = limpieza;
    }
}

