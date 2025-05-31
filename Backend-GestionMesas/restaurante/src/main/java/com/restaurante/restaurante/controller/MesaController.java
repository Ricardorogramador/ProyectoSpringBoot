package com.restaurante.restaurante.controller;

import com.restaurante.restaurante.model.Mesa;
import com.restaurante.restaurante.service.MesaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    public List<Mesa> listarMesas() {
        return mesaService.listarMesa();
    }
    @PostMapping
    public Mesa crearMesa(@RequestBody Mesa mesa) {
        return mesaService.crearMesa(mesa);
    }

    @PutMapping("/{id}")
    public Mesa actualizarMesa(@PathVariable Long id, @RequestBody Mesa mesa) {
        return mesaService.actualizarMesa(id, mesa);
    }
    @DeleteMapping("/{id}")
    public void eliminarMesa(@PathVariable Long id) {
        mesaService.eliminarMesa(id);
    }
@PostMapping("/preset")
    public void presetMesas(@RequestParam int cantidad) {
        mesaService.presetMesas(cantidad);
}
    @PutMapping("/{id}/estado")
    public Mesa cambiarEstadoMesa(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        return mesaService.cambiarEstadoMesa(id, estado);
    }
}
