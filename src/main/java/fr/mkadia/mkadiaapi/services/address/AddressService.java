package fr.mkadia.mkadiaapi.services.address;

import fr.mkadia.mkadiaapi.dtos.AddressDTO;
import fr.mkadia.mkadiaapi.entities.Address;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.exceptions.UnauthorizedException;
import fr.mkadia.mkadiaapi.mappers.AddressMapper;
import fr.mkadia.mkadiaapi.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    /**
     * Récupère toutes les adresses d'un utilisateur
     */
    @Transactional
    public List<AddressDTO> getAddresses(User user) {
        return addressRepository.getAddressByUserId(user.getId())
                .stream()
                .map(addressMapper::fromEntity)
                .toList();
    }

    /**
     * Crée une nouvelle adresse
     */
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = addressMapper.fromDTO(addressDTO);
        address.setUser(user);

        // Si c'est la première adresse ou si elle est marquée par défaut
        if (user.getAddresses().isEmpty() || Boolean.TRUE.equals(addressDTO.getIsDefault())) {
            // Retirer le flag "default" des autres adresses
            removeDefaultFromOtherAddresses(user);
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }

        Address savedAddress = addressRepository.save(address);
        return addressMapper.fromEntity(savedAddress);
    }

    /**
     * Définit une adresse comme adresse par défaut
     */
    @Transactional
    public AddressDTO setDefaultAddress(Integer id, User user) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adresse non trouvée"));

        // Vérifier que l'adresse appartient bien à l'utilisateur
        if (!address.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Vous n'avez pas accès à cette adresse");
        }

        // Retirer le flag "default" des autres adresses
        removeDefaultFromOtherAddresses(user);

        // Définir cette adresse comme par défaut
        address.setIsDefault(true);
        Address updatedAddress = addressRepository.save(address);

        return addressMapper.fromEntity(updatedAddress);
    }

    /**
     * Supprime une adresse
     */
    @Transactional
    public void deleteAddress(Integer id, User user) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adresse non trouvée"));

        // Vérifier que l'adresse appartient bien à l'utilisateur
        if (!address.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Vous n'avez pas accès à cette adresse");
        }

        // Si l'adresse est par défaut et qu'il y a d'autres adresses
        boolean wasDefault = Boolean.TRUE.equals(address.getIsDefault());
        addressRepository.delete(address);

        // Si c'était l'adresse par défaut, définir une autre adresse comme par défaut
        if (wasDefault) {
            List<Address> remainingAddresses = addressRepository.getAddressByUserId(user.getId());
            if (!remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }
        }
    }

    /**
     * Retire le flag "default" de toutes les adresses d'un utilisateur
     */
    private void removeDefaultFromOtherAddresses(User user) {
        List<Address> addresses = user.getAddresses();

        addresses.forEach(addr -> {
            if (Boolean.TRUE.equals(addr.getIsDefault())) {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            }
        });
    }
}