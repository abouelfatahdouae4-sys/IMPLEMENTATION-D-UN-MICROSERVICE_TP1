package ma.emsi.ebankservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.emsi.ebankservice.enums.TypeCompte;

import java.util.Date;

/**
 * 2. Entité JPA Compte
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compte {
    @Id
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    private Double solde;

    private String devise;

    @Enumerated(EnumType.STRING)
    private TypeCompte type;

    @ManyToOne
    private Client client;
}
