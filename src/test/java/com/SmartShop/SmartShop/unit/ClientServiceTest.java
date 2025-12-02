package com.SmartShop.SmartShop.unit;

import com.SmartShop.SmartShop.dto.ClientDTO;
import com.SmartShop.SmartShop.dto.CommandeDTO;
import com.SmartShop.SmartShop.dto.RegisterDto;
import com.SmartShop.SmartShop.dto.Stat;
import com.SmartShop.SmartShop.entity.Client;
import com.SmartShop.SmartShop.entity.Commande;
import com.SmartShop.SmartShop.entity.User;
import com.SmartShop.SmartShop.entity.enums.CustomerTier;
import com.SmartShop.SmartShop.entity.enums.UserRole;
import com.SmartShop.SmartShop.exception.BusinessException;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.ClientRepository;
import com.SmartShop.SmartShop.repository.UserRepository;
import com.SmartShop.SmartShop.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SmartShopMapper mapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    private RegisterDto registerDto;
    private User user;
    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setPassword("password123");
        registerDto.setEmail("test@example.com");
        registerDto.setNom("Test User");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.CLIENT);

        client = new Client();
        client.setId(1L);
        client.setNom("Test User");
        client.setEmail("test@example.com");
        client.setNiveau(CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(0.0);
        client.setUser(user);
        client.setCommandes(new ArrayList<>());

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setNom("Test User");
        clientDTO.setEmail("test@example.com");
    }

    @Test
    void createClient_success() {
        // Given
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(clientRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> {
            Client c = i.getArgument(0);
            c.setId(1L);
            return c;
        });
        when(mapper.toClientDTO(any(Client.class))).thenReturn(clientDTO);

        // When
        ClientDTO result = clientService.createClient(registerDto);

        // Then
        assertNotNull(result);
        assertEquals("Test User", result.getNom());
        verify(userRepository, times(1)).existsByUsername(registerDto.getUsername());
        verify(clientRepository, times(1)).existsByEmail(registerDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void createClient_usernameExists() {
        // Given
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> clientService.createClient(registerDto)
        );

        assertTrue(exception.getMessage().contains("Username already exists"));
        verify(userRepository, times(1)).existsByUsername(registerDto.getUsername());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void createClient_emailExists() {
        // Given
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(clientRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> clientService.createClient(registerDto)
        );

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void getClientById_success() {
        // Given
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(mapper.toClientDTO(client)).thenReturn(clientDTO);

        // When
        ClientDTO result = clientService.getClientById(clientId);

        // Then
        assertNotNull(result);
        verify(clientRepository, times(1)).findById(clientId);
        verify(mapper, times(1)).toClientDTO(client);
    }

    @Test
    void getClientById_notFound() {
        // Given
        Long clientId = 999L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.getClientById(clientId)
        );

        assertTrue(exception.getMessage().contains("Client not found"));
    }

    @Test
    void updateClient_success() {
        // Given
        Long clientId = 1L;
        ClientDTO updatedDTO = new ClientDTO();
        updatedDTO.setNom("Updated Name");
        updatedDTO.setEmail("updated@example.com");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toClientDTO(any(Client.class))).thenReturn(updatedDTO);

        // When
        ClientDTO result = clientService.updateClient(clientId, updatedDTO);

        // Then
        assertNotNull(result);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void deleteClient_success() {
        // Given
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        // When
        clientService.deleteClient(clientId);

        // Then
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void getClientByUserId_success() {
        // Given
        Long userId = 1L;
        when(clientRepository.findByUserId(userId)).thenReturn(Optional.of(client));
        when(mapper.toClientDTO(client)).thenReturn(clientDTO);

        // When
        ClientDTO result = clientService.getClientByUserId(userId);

        // Then
        assertNotNull(result);
        verify(clientRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getClientStat_success() {
        // Given
        Long userId = 1L;
        Commande commande1 = new Commande();
        commande1.setTotal(100.0);
        Commande commande2 = new Commande();
        commande2.setTotal(200.0);
        client.setCommandes(List.of(commande1, commande2));

        when(clientRepository.findByUserId(userId)).thenReturn(Optional.of(client));

        // When
        Stat result = clientService.getClientStat(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getCommansCount());
        assertEquals(300.0, result.getMoantCumule());
    }
}

