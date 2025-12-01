package com.SmartShop.SmartShop.service;

import com.SmartShop.SmartShop.dto.CommandeCreateRequest;
import com.SmartShop.SmartShop.dto.CommandeDTO;
import com.SmartShop.SmartShop.dto.OrderItemRequest;
import com.SmartShop.SmartShop.entity.Client;
import com.SmartShop.SmartShop.entity.Commande;
import com.SmartShop.SmartShop.entity.OrderItem;
import com.SmartShop.SmartShop.entity.Product;
import com.SmartShop.SmartShop.entity.enums.OrderStatus;
import com.SmartShop.SmartShop.exception.BusinessException;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.ClientRepository;
import com.SmartShop.SmartShop.repository.CommandeRepository;
import com.SmartShop.SmartShop.repository.ProductRepository;
import com.SmartShop.SmartShop.service.helper.RetreiveDiscount;
import com.SmartShop.SmartShop.service.helper.UpdateC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final SmartShopMapper mapper;
    private final RetreiveDiscount retreiveDiscount;


    @Transactional
    public CommandeDTO createCommande(CommandeCreateRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + request.getClientId()));


        Commande commande = new Commande();
        commande.setClient(client);
        commande.setDate(LocalDateTime.now());
        commande.setStatus(OrderStatus.PENDING);
        commande.setPromoCode(null);
        commande.setItems(new ArrayList<>());

        List<OrderItem> items = new ArrayList<>();

        double sousTotal = 0;
        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() ->  new ResourceNotFoundException("Product not found: " + request.getClientId()));

            if (product.getStockDisponible() < itemReq.getQuantity()) {
                commande.setStatus(OrderStatus.REJECTED);
                throw new BusinessException("Insufficient stock for product: " + product.getNom());

            }
            if ( product.isDeleted()) {
                commande.setStatus(OrderStatus.REJECTED);
                throw new BusinessException("Product is deleted: " + product.getNom());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantite(itemReq.getQuantity());
            orderItem.setPrixUnitaire(product.getPrixUnitaire());
            orderItem.setTotalLigne(product.getPrixUnitaire() * itemReq.getQuantity());
            orderItem.setCommande(commande);

            items.add(orderItem);
            sousTotal += orderItem.getTotalLigne();


        }
        commande.setItems(items);
        double remise = retreiveDiscount.getClientDiscount(client.getNiveau(), sousTotal);
        double promoDiscount = retreiveDiscount.getPromoDiscount(request.getPromoCode(), sousTotal);
        double tvaAmount = (sousTotal - remise - promoDiscount) * request.getTva() / 100.0;
        double total = sousTotal - remise - promoDiscount + tvaAmount;


        commande.setSousTotal(sousTotal);
        commande.setRemise(remise);
        commande.setTva(request.getTva());
        commande.setTotal(total);
        commande.setMontantRestant(total);
        Commande saved = commandeRepository.save(commande);

        return mapper.toCommandeDTO(saved);
    }

    @Transactional
    public CommandeDTO confirmCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found: " + commandeId));


        if (commande.getMontantRestant()!=0 ){
            throw new BusinessException("Payment is not completed yet");
        }
        if (commande.getStatus().equals(OrderStatus.CANCELED)){
            throw new BusinessException("Commande already canceled");
        }
        for (OrderItem item : commande.getItems()) {
            Product product = item.getProduct();

            if (product.getStockDisponible() < item.getQuantite()) {
                throw new BusinessException("Insufficient stock for product: " + product.getNom());
            }
        }


        for (OrderItem item : commande.getItems()) {
            Product product = item.getProduct();

            product.setStockDisponible(product.getStockDisponible() - item.getQuantite());
            productRepository.save(product);
        }




        Client c =  commande.getClient();

        UpdateC.updateClientData(c , commande);

        commande.setStatus(OrderStatus.CONFIRMED);

        return mapper.toCommandeDTO(commandeRepository.save(commande));
    }

    @Transactional
    public CommandeDTO cancelCommande(Long commandeId) {

        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() ->new ResourceNotFoundException("Commande not found: " + commandeId));

        OrderStatus oldStatus = commande.getStatus();
        if (oldStatus == OrderStatus.CONFIRMED){
            throw new BusinessException("command is already confirmed");
        }




            commande.getItems().clear();



        commande.setStatus(OrderStatus.CANCELED);
        commandeRepository.save(commande);

        return mapper.toCommandeDTO(commande);
    }


    public CommandeDTO rejeterCommande(Long commandeId){


        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found: " + commandeId));

        OrderStatus oldStatus = commande.getStatus();
        if (oldStatus == OrderStatus.CONFIRMED){
            throw new BusinessException("command is already confirmed");
        }

        commande.getItems().clear();



        commande.setStatus(OrderStatus.REJECTED);
        commandeRepository.save(commande);

        return mapper.toCommandeDTO(commande);

    }





    public CommandeDTO getCommandeById(Long commandeId) {
        return commandeRepository.findById(commandeId)
                .map(mapper::toCommandeDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found: " + commandeId));
    }

    public List<CommandeDTO> getAllCommandes() {
        return commandeRepository.findAll().stream()
                .map(mapper::toCommandeDTO)
                .toList();
    }

    public List<CommandeDTO> getCommandesByClient(Long clientId) {
        return commandeRepository.findByClientId(clientId).stream()
                .map(mapper::toCommandeDTO)
                .toList();
    }

}
