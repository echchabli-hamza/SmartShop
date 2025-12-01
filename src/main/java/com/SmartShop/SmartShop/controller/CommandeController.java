package com.SmartShop.SmartShop.controller;



import com.SmartShop.SmartShop.dto.CommandeCreateRequest;
import com.SmartShop.SmartShop.dto.CommandeDTO;
import com.SmartShop.SmartShop.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public CommandeDTO createCommande(@RequestBody CommandeCreateRequest request) {
        return commandeService.createCommande(request);
    }


    @GetMapping("/{id}")
    public CommandeDTO getCommande(@PathVariable Long id) {
        return commandeService.getCommandeById(id);
    }


    @GetMapping
    public List<CommandeDTO> getAllCommandes() {
        return commandeService.getAllCommandes();
    }


    @PutMapping("/{id}/status")
    public CommandeDTO updateStatus(@PathVariable Long id) {
        return commandeService.confirmCommande(id);
    }


    @PutMapping("/{id}/cancel")
    public CommandeDTO deleteCommande(@PathVariable Long id) {
       return commandeService.cancelCommande(id);
    }



    @PutMapping("/{id}/rejeter")
    public ResponseEntity<CommandeDTO> rejeterCommande(@PathVariable Long id) {
        CommandeDTO res =  commandeService.rejeterCommande(id);
        return ResponseEntity.ok(res);
    }
}

