package com.restaurante.restaurante.service;

import com.restaurante.restaurante.model.Mesa;
import com.restaurante.restaurante.repository.MesaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {
    private final  MesaRepository mesaRepository;
    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }
    public List<Mesa> listarMesa() {
        return mesaRepository.findAll();
    }
    public Mesa crearMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }
    public Mesa actualizarMesa(Long id, Mesa nuevaMesa) {
        return mesaRepository.findById(id)
                .map(m -> {
                    m.setNumero(nuevaMesa.getNumero());
                    m.setOcupada(nuevaMesa.isOcupada());
                    return mesaRepository.save(m);
                })
                .orElse(null);
    }
    public void eliminarMesa(Long id) {
        mesaRepository.deleteById(id);
    }
    public void presetMesas(int cantidad) {
        List<Mesa> todas = mesaRepository.findAll();
        int actuales = todas.size();

        // Eliminar mesas sobrantes (por número)
        if (actuales > cantidad) {
            List<Mesa> aEliminar = todas.stream()
                    .filter(m -> m.getNumero() > cantidad)
                    .collect(Collectors.toList());
            mesaRepository.deleteAll(aEliminar);
        }

        // Agregar mesas nuevas si faltan
        if (actuales < cantidad) {
            for (int i = actuales + 1; i <= cantidad; i++) {
                Mesa nueva = new Mesa();
                nueva.setNumero(i);
                nueva.setOcupada(false);
                nueva.setLimpieza(false);
                mesaRepository.save(nueva);
            }
        }
    }
    public Mesa cambiarEstadoMesa(Long id, String estado) {
        Mesa mesa = mesaRepository.findById(id).orElseThrow();
        switch (estado.toLowerCase()) {
            case "libre":
                mesa.setOcupada(false);
                mesa.setLimpieza(false);
                break;
            case "ocupado":
                mesa.setOcupada(true);
                mesa.setLimpieza(false);
                break;
            case "limpieza":
                mesa.setOcupada(false);
                mesa.setLimpieza(true);
                break;
            default:
                throw new IllegalArgumentException("Estado inválido");
        }
        mesaRepository.save(mesa);
        return mesa;
    }
}
