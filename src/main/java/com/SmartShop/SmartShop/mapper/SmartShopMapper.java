package com.SmartShop.SmartShop.mapper;

import com.SmartShop.SmartShop.dto.*;
import com.SmartShop.SmartShop.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SmartShopMapper {

    SmartShopMapper INSTANCE = Mappers.getMapper(SmartShopMapper.class);


    UserDTO toUserDTO(User user);

    User toUser(UserDTO dto);


    ClientDTO toClientDTO(Client client);

    Client toClient(ClientDTO dto);


    ProductDTO toProductDTO(Product product);

    Product toProduct(ProductDTO dto);


    @Mapping(source = "produit.id", target = "produitId")
    @Mapping(source = "produit.nom", target = "produitNom")
    OrderItemDTO toOrderItemDTO(OrderItem item);

    OrderItem toOrderItem(OrderItemDTO dto);


    PaiementDTO toPaiementDTO(Paiement paiement);

    Paiement toPaiement(PaiementDTO dto);


    PromoCodeDTO toPromoCodeDTO(PromoCode promo);

    PromoCode toPromoCode(PromoCodeDTO dto);


    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "promoCode", target = "promoCode")
    CommandeDTO toCommandeDTO(Commande commande);

    Commande toCommande(CommandeDTO dto);
}
