package ma.emsi.ebankservice.services;

import ma.emsi.ebankservice.dto.CompteRequestDTO;
import ma.emsi.ebankservice.dto.CompteResponseDTO;

import java.util.List;

/**
 * 10. Couche service (métier)
 */
public interface CompteService {
    List<CompteResponseDTO> listerComptes();
    CompteResponseDTO consulterCompte(String id);
    CompteResponseDTO ajouterCompte(CompteRequestDTO requestDTO);
    CompteResponseDTO modifierCompte(String id, CompteRequestDTO requestDTO);
    void supprimerCompte(String id);
}
