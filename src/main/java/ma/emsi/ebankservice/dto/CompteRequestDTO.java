package ma.emsi.ebankservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.ebankservice.enums.TypeCompte;

/**
 * 9. DTO reçu par le micro-service lors de la création / modification d'un compte
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteRequestDTO {
    private Double solde;
    private String devise;
    private TypeCompte type;
}
