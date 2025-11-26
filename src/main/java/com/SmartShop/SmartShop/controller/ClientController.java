package com.SmartShop.SmartShop.controller;


import com.SmartShop.SmartShop.dto.*;
import com.SmartShop.SmartShop.entity.enums.UserRole;
import com.SmartShop.SmartShop.helper.AuthHelper;
import com.SmartShop.SmartShop.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/clients")
public class ClientController {

    private final ClientService clientService;
    private final AuthHelper authHelper ;

    public ClientController(ClientService clientService, AuthHelper authHelper) {
        this.clientService = clientService;
        this.authHelper = authHelper;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientDTO> registerClient(@RequestBody RegisterDto registerDto  ) {

        ClientDTO clientDTO = clientService.createClient(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientDTO);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id ,  HttpSession session) {

        ClientDTO clientDTO = clientService.getClientById(id);
        return ResponseEntity.ok(clientDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
      //  authHelper.checkAuthenticatedUserRole(session , UserRole.ADMIN);
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
       // authHelper.checkAuthenticatedUserRole(session , UserRole.ADMIN);
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
