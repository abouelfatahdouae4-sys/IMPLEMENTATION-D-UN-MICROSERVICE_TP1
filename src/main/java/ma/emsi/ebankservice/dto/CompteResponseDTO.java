package ma.emsi.ebankservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.ebankservice.enums.TypeCompte;

import java.util.Date;

/**
 * 9. DTO renvoyé par le micro-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteResponseDTO {
    private String id;
    private Date dateCreation;
    private Double solde;
    private String devise;
    private TypeCompte type;
}
