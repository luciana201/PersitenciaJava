package pio.daw.ra8.proyecto.model;

import jakarta.persistence.*;

@Entity
public class Individuo {

    @Id
    @GeneratedValue
    private long id;
    private String nombre;
    private double saldoActual;
    private double saldoInicial;

    @ManyToOne(fetch = FetchType.LAZY)
    private Simulacion simulacion;

    public Individuo() {}

    public Individuo(String nombre, double saldoInicial, Simulacion simulacion) {
        this.nombre = nombre;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoInicial;
        this.simulacion = simulacion;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public double getSaldoActual() { return saldoActual; }
    public void setSaldoActual(double s) { this.saldoActual = s; }
    public double getSaldoInicial() { return saldoInicial; }
    public Simulacion getSimulacion() { return simulacion; }

    @Override
    public String toString() {
        return "Individuo{nombre='" + nombre + "', saldo=" + String.format("%.2f", saldoActual) + "}";
    }
}