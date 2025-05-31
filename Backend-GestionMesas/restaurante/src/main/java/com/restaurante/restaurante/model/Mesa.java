package com.restaurante.restaurante.model;

import jakarta.persistence.*;

@Entity
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numero;
    private boolean ocupada;
    private boolean Limpieza;

    public Mesa() {}

    public Mesa(int numero, boolean ocupada) {
        this.id = id;
        this.numero = numero;
        this.ocupada = ocupada;
        this.Limpieza = Limpieza;
    }

    public Long getId() {
        return id;
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
