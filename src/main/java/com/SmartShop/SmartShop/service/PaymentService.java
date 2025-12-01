package com.SmartShop.SmartShop.service;

import com.SmartShop.SmartShop.dto.PaiementDTO;
import com.SmartShop.SmartShop.entity.Commande;
import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.entity.enums.PaymentType;
import com.SmartShop.SmartShop.exception.BusinessException;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.CommandeRepository;
import com.SmartShop.SmartShop.repository.PaiementRepository;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;
import com.SmartShop.SmartShop.service.strategy.impl.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaiementRepository paiementRepository;
    private final PaymentStrategyFactory strategyFactory;
    private final SmartShopMapper smartShopMapper ;
    private  final CommandeRepository commandeRepository ;

    @Transactional
    public PaiementDTO addPayment(PaiementDTO paiementDTO) {



        Commande commande = commandeRepository.findById((long) paiementDTO.getCommandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable with id : " +  paiementDTO.getCommandeId()));
        if (paiementDTO.getMontant() > commande.getMontantRestant()) {
            throw new BusinessException(
                    "Le montant du paiement dépasse le montant restant à payer : " + commande.getMontantRestant() + " MAD"
            );
        }

          Paiement paiement = smartShopMapper.toPaiement(paiementDTO);
        int nextPaymentNumber = commande.getPaiements().size() + 1;
        paiement.setNumeroPaiement(nextPaymentNumber);

        PaymentStrategy strategy = strategyFactory.getStrategy(PaymentType.valueOf(paiementDTO.getTypePaiement()));
        strategy.addPayment(paiement);

        paiement.setCommande(commande);
       Paiement res =  paiementRepository.save(paiement);
        return smartShopMapper.toPaiementDTO(res);
    }

    @Transactional
    public PaiementDTO encaisserPayment(Long paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() ->new ResourceNotFoundException("Payment not found with id : " +  paiementId));
        PaymentStrategy strategy = strategyFactory.getStrategy(paiement.getTypePaiement());
        strategy.encaisserPayment(paiement);
        Commande cres = paiement.getCommande();
        cres.setMontantRestant(cres.getMontantRestant()-paiement.getMontant());
        commandeRepository.save(cres);
        Paiement res =  paiementRepository.save(paiement);
        return smartShopMapper.toPaiementDTO(res);
    }

    @Transactional
    public PaiementDTO rejectPayment(Long paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() ->new ResourceNotFoundException("Payment not found with id : " +  paiementId));
        if (paiement.getStatut().equals(PaymentStatus.ENCAISSE)){
            throw new BusinessException("Payment already confirmed");
        }
        PaymentStrategy strategy = strategyFactory.getStrategy(paiement.getTypePaiement());
        strategy.rejectPayment(paiement);
        Paiement res =  paiementRepository.save(paiement);
        return smartShopMapper.toPaiementDTO(res);
    }

    public List<Paiement> getAll() {
        return paiementRepository.findAll();
    }
}
