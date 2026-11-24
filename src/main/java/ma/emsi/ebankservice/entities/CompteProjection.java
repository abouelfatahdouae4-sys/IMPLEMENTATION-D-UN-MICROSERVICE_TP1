package ma.emsi.ebankservice.entities;

import ma.emsi.ebankservice.enums.TypeCompte;
import org.springframework.data.rest.core.config.Projection;

/**
 * 8. Projection Spring Data Rest : n'expose que certains attributs d'un compte.
 * Utilisation : GET /v1/comptes?projection=p1
 */
@Projection(types = Compte.class, name = "p1")
public interface CompteProjection {
    String getId();
    Double getSolde();
    TypeCompte getType();
}
