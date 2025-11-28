package com.SmartShop.SmartShop.unit;

import com.SmartShop.SmartShop.dto.CommandeCreateRequest;
import com.SmartShop.SmartShop.dto.CommandeDTO;
import com.SmartShop.SmartShop.dto.OrderItemRequest;
import com.SmartShop.SmartShop.entity.Client;
import com.SmartShop.SmartShop.entity.Product;
import com.SmartShop.SmartShop.entity.enums.CustomerTier;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.ClientRepository;
import com.SmartShop.SmartShop.repository.CommandeRepository;
import com.SmartShop.SmartShop.repository.ProductRepository;
import com.SmartShop.SmartShop.service.CommandeService;
import com.SmartShop.SmartShop.service.helper.RetreiveDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SmartShopMapper mapper;

    @Mock
    private RetreiveDiscount retreiveDiscount;

    @InjectMocks
    private CommandeService commandeService;

    private Client client;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client();
        client.setId(1L);
        client.setNiveau(CustomerTier.SILVER);

        product = new Product();
        product.setId(1L);
        product.setNom("Product A");
        product.setPrixUnitaire(100.0);
        product.setStockDisponible(10);
    }

    @Test
    void createCommande_success() {

        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(product.getId());
        itemReq.setQuantity(5);

        CommandeCreateRequest request = new CommandeCreateRequest();
        request.setClientId(client.getId());
        request.setItems(List.of(itemReq));
        request.setTva(20.0);
        request.setPromoCode(null);

        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(retreiveDiscount.getClientDiscount(client.getNiveau(), 500.0)).thenReturn(25.0);
        when(retreiveDiscount.getPromoDiscount(null, 500.0)).thenReturn(0.0);
        when(commandeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(mapper.toCommandeDTO(any())).thenReturn(new CommandeDTO());

        CommandeDTO result = commandeService.createCommande(request);

        assertNotNull(result);
        verify(commandeRepository, times(1)).save(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void createCommande_insufficientStock() {

        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(product.getId());
        itemReq.setQuantity(20);

        CommandeCreateRequest request = new CommandeCreateRequest();
        request.setClientId(client.getId());
        request.setItems(List.of(itemReq));

        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commandeService.createCommande(request);
        });

        assertTrue(exception.getMessage().contains("Insufficient stock"));
        verify(commandeRepository, never()).save(any());
    }

    @Test
    void createCommande_clientNotFound() {

        CommandeCreateRequest request = new CommandeCreateRequest();
        request.setClientId(999L);

        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commandeService.createCommande(request);
        });

        assertTrue(exception.getMessage().contains("Client not found"));
        verify(commandeRepository, never()).save(any());
    }
}
