package pio.daw.ra8.proyecto.service;

import jakarta.persistence.EntityManager;
import pio.daw.ra8.proyecto.model.Individuo;
import pio.daw.ra8.proyecto.model.Intercambio;
import pio.daw.ra8.proyecto.model.Simulacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulacionService {

    private final EntityManager em;
    private final Random random = new Random();

    public SimulacionService(EntityManager em) {
        this.em = em;
    }

    public Simulacion ejecutar(String nombre, int numIndividuos,
                               double saldoInicial, int numRondas) {

        // 1. Crear y persistir simulación e individuos
        Simulacion sim = new Simulacion(nombre, numRondas, numIndividuos, saldoInicial);
        em.getTransaction().begin();
        em.persist(sim);

        List<Individuo> individuos = new ArrayList<>();
        for (int i = 0; i < numIndividuos; i++) {
            Individuo ind = new Individuo("Individuo_" + i, saldoInicial, sim);
            em.persist(ind);
            individuos.add(ind);
        }
        em.getTransaction().commit();

        // 2. Bucle de rondas con commits cada 100
        em.getTransaction().begin();

        for (int ronda = 0; ronda < numRondas; ronda++) {

            int idxA = random.nextInt(numIndividuos);
            int idxB;
            do { idxB = random.nextInt(numIndividuos); } while (idxB == idxA);

            Individuo a = individuos.get(idxA);
            Individuo b = individuos.get(idxB);

            double saldoMin = Math.min(a.getSaldoActual(), b.getSaldoActual());
            if (saldoMin < 1) continue;

            double importe = 1 + random.nextDouble() * (saldoMin - 1);

            Individuo emisor, receptor;
            if (random.nextBoolean()) { emisor = a; receptor = b; }
            else                      { emisor = b; receptor = a; }

            emisor.setSaldoActual(emisor.getSaldoActual() - importe);
            receptor.setSaldoActual(receptor.getSaldoActual() + importe);

            em.persist(new Intercambio(ronda, importe, emisor, receptor, sim));

            // Commit cada 100 rondas
            if ((ronda + 1) % 100 == 0) {
                em.getTransaction().commit();
                em.clear();
                em.getTransaction().begin();

                sim = em.find(Simulacion.class, sim.getId());
                for (int i = 0; i < individuos.size(); i++) {
                    individuos.set(i, em.find(Individuo.class, individuos.get(i).getId()));
                }
            }
        }

        if (em.getTransaction().isActive()) em.getTransaction().commit();

        return em.find(Simulacion.class, sim.getId());
    }
}