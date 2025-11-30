package com.SmartShop.SmartShop.controller;

import com.SmartShop.SmartShop.dto.PaiementDTO;
import com.SmartShop.SmartShop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/paiements")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping
    public ResponseEntity<PaiementDTO> addPayment(@RequestBody PaiementDTO dto) {
        PaiementDTO saved = paymentService.addPayment(dto);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}/encaisser")
    public ResponseEntity<PaiementDTO> encaisser(@PathVariable Long id) {
        PaiementDTO updated = paymentService.encaisserPayment(id);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/rejeter")
    public ResponseEntity<PaiementDTO> reject(@PathVariable Long id) {
        PaiementDTO updated = paymentService.rejectPayment(id);
        return ResponseEntity.ok(updated);
    }






    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }
}
