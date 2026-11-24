package ma.emsi.ebankservice.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebankservice.dto.CompteRequestDTO;
import ma.emsi.ebankservice.dto.CompteResponseDTO;
import ma.emsi.ebankservice.entities.Compte;
import ma.emsi.ebankservice.exceptions.CompteNotFoundException;
import ma.emsi.ebankservice.mappers.CompteMapper;
import ma.emsi.ebankservice.repositories.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CompteServiceImpl implements CompteService {

    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;

    @Override
    public List<CompteResponseDTO> listerComptes() {
        return compteRepository.findAll().stream()
                .map(compteMapper::fromCompte)
                .toList();
    }

    @Override
    public CompteResponseDTO consulterCompte(String id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new CompteNotFoundException(id));
        return compteMapper.fromCompte(compte);
    }

    @Override
    public CompteResponseDTO ajouterCompte(CompteRequestDTO requestDTO) {
        Compte compte = Compte.builder()
                .id(UUID.randomUUID().toString())
                .dateCreation(new Date())
                .solde(requestDTO.getSolde())
                .devise(requestDTO.getDevise())
                .type(requestDTO.getType())
                .build();
        return compteMapper.fromCompte(compteRepository.save(compte));
    }

    /**
     * Mise à jour partielle : seuls les champs non nuls sont modifiés.
     */
    @Override
    public CompteResponseDTO modifierCompte(String id, CompteRequestDTO requestDTO) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new CompteNotFoundException(id));
        if (requestDTO.getSolde() != null) compte.setSolde(requestDTO.getSolde());
        if (requestDTO.getDevise() != null) compte.setDevise(requestDTO.getDevise());
        if (requestDTO.getType() != null) compte.setType(requestDTO.getType());
        return compteMapper.fromCompte(compteRepository.save(compte));
    }

    @Override
    public void supprimerCompte(String id) {
        if (!compteRepository.existsById(id)) throw new CompteNotFoundException(id);
        compteRepository.deleteById(id);
    }
}
