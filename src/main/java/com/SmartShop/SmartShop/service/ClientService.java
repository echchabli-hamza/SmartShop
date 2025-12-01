package com.SmartShop.SmartShop.service;



import com.SmartShop.SmartShop.dto.ClientDTO;
import com.SmartShop.SmartShop.dto.CommandeDTO;
import com.SmartShop.SmartShop.dto.RegisterDto;
import com.SmartShop.SmartShop.dto.Stat;
import com.SmartShop.SmartShop.entity.*;
import com.SmartShop.SmartShop.entity.enums.CustomerTier;
import com.SmartShop.SmartShop.entity.enums.UserRole;
import com.SmartShop.SmartShop.exception.BusinessException;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.*;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service

@Transactional
public class ClientService  {

    private final UserRepository userRepository ;
    private final ClientRepository clientRepository;
    private final SmartShopMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;


    public ClientService(UserRepository userRepository,
                         ClientRepository clientRepository,
                         SmartShopMapper mapper,
                         BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }


    public ClientDTO createClient(RegisterDto registerDto) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BusinessException("Username already exists" + registerDto.getUsername());
        }
        if (clientRepository.existsByEmail(registerDto.getEmail())) {
            throw new BusinessException("Email already exists" + registerDto.getEmail());

        }


        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(UserRole.CLIENT);
        User savedUser = userRepository.save(user);


        Client client = new Client();
        client.setNom(registerDto.getNom());
        client.setEmail(registerDto.getEmail());
        client.setNiveau(CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(0.0);
        client.setUser(savedUser);

        Client savedClient = clientRepository.save(client);
        return mapper.toClientDTO(savedClient);
    }


    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return mapper.toClientDTO(client);
    }


    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(mapper::toClientDTO)
                .collect(Collectors.toList());
    }


    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Client not found with id: " + id));


        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());

        Client updated = clientRepository.save(client);
        return mapper.toClientDTO(updated);
    }


    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->   new ResourceNotFoundException("Client not found with id: " + id));
        clientRepository.delete(client);
    }


    public  ClientDTO getClientByUserId(Long id) {

        return mapper.toClientDTO(clientRepository.findByUserId(id).get());

    }

    public List<CommandeDTO> getClientC(Long id){

        Client c =clientRepository.findByUserId(id).get();
        return c.getCommandes().stream().map(mapper::toCommandeDTO).toList();

    }
    public Stat getClientStat(Long id){

        Client c =clientRepository.findByUserId(id).get();
        Stat s = new Stat();
       int count = (int)c.getCommandes().stream().count();
       double d =c.getCommandes().stream().mapToDouble(Commande::getTotal).sum();

        s.setCommansCount(count);
        s.setMoantCumule(d);

        return s;


    }

}

