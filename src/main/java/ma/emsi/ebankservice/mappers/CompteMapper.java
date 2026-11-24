package ma.emsi.ebankservice.mappers;

import ma.emsi.ebankservice.dto.CompteRequestDTO;
import ma.emsi.ebankservice.dto.CompteResponseDTO;
import ma.emsi.ebankservice.entities.Compte;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 9. Mapper Entité <-> DTO
 */
@Component
public class CompteMapper {

    public CompteResponseDTO fromCompte(Compte compte) {
        CompteResponseDTO dto = new CompteResponseDTO();
        BeanUtils.copyProperties(compte, dto);
        return dto;
    }

    public Compte fromRequestDTO(CompteRequestDTO requestDTO) {
        Compte compte = new Compte();
        BeanUtils.copyProperties(requestDTO, compte);
        return compte;
    }
}
