package ma.emsi.ebankservice.repositories;

import ma.emsi.ebankservice.entities.Compte;
import ma.emsi.ebankservice.enums.TypeCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * 3. Interface CompteRepository basée sur Spring Data
 * 8. Exposée automatiquement en API Restful par Spring Data Rest (/v1/comptes)
 */
@RepositoryRestResource(path = "comptes")
public interface CompteRepository extends JpaRepository<Compte, String> {

    // GET /v1/comptes/search/byType?t=COMPTE_COURANT
    @RestResource(path = "byType")
    List<Compte> findByType(@Param("t") TypeCompte type);

    // GET /v1/comptes/search/byDevise?d=MAD
    @RestResource(path = "byDevise")
    List<Compte> findByDevise(@Param("d") String devise);

    @RestResource(exported = false)
    List<Compte> findByClientId(Long clientId);
}
