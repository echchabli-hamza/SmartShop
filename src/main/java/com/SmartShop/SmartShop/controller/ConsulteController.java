package com.SmartShop.SmartShop.controller;



import com.SmartShop.SmartShop.dto.ClientDTO;
import com.SmartShop.SmartShop.dto.CommandeDTO;
import com.SmartShop.SmartShop.dto.PaiementDTO;
import com.SmartShop.SmartShop.dto.ProductDTO;
import com.SmartShop.SmartShop.entity.User;
import com.SmartShop.SmartShop.service.ClientService;
import com.SmartShop.SmartShop.service.CommandeService;
import com.SmartShop.SmartShop.service.PaymentService;
import com.SmartShop.SmartShop.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ConsulteController {

    private final ClientService clientService;
    private final  ProductService productService;


    public ConsulteController(ClientService clientService,

                              ProductService productService, ProductService productService1) {
        this.clientService = clientService;

        this.productService = productService1;
    }


    @GetMapping("/profile")
    public ResponseEntity<ClientDTO> getProfile(HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(clientService.getClientByUserId(user.getId()));
    }


    @GetMapping("/orders")
    public ResponseEntity<List<CommandeDTO>> getOrders(HttpServletRequest request, Pageable pageable) {
        User user = (User) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(clientService.getClientC(user.getId()));
    }





    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Integer q,

            Pageable pageable) {
        return ResponseEntity.ok(productService.getProducts(nom, minPrice, q,pageable));
    }


    @GetMapping("/stats")
    public ResponseEntity<?> getStats(HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(clientService.getClientStat(user.getId()));
    }
}
