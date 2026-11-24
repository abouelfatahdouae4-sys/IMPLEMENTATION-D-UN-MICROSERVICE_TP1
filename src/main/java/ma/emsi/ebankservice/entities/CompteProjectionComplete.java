package ma.emsi.ebankservice.entities;

import ma.emsi.ebankservice.enums.TypeCompte;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

/**
 * 8. Deuxième projection : toutes les infos du compte + le nom du client.
 * Utilisation : GET /v1/comptes?projection=p2
 */
@Projection(types = Compte.class, name = "p2")
public interface CompteProjectionComplete {
    String getId();
    Date getDateCreation();
    Double getSolde();
    String getDevise();
    TypeCompte getType();
    ClientNom getClient();

    interface ClientNom {
        String getNom();
    }
}
