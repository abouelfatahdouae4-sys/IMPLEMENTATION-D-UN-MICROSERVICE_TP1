package ma.emsi.ebankservice.repositories;

import ma.emsi.ebankservice.entities.Compte;
import ma.emsi.ebankservice.enums.TypeCompte;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 4. Tests unitaires de la couche DAO
 */
@DataJpaTest
class CompteRepositoryTest {

    @Autowired
    private CompteRepository compteRepository;

    private Compte nouveauCompte(TypeCompte type, String devise) {
        return Compte.builder()
                .id(UUID.randomUUID().toString())
                .dateCreation(new Date())
                .solde(5000.0)
                .devise(devise)
                .type(type)
                .build();
    }

    @Test
    void doitSauvegarderEtRetrouverUnCompte() {
        Compte saved = compteRepository.save(nouveauCompte(TypeCompte.COMPTE_COURANT, "MAD"));
        Optional<Compte> found = compteRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getSolde()).isEqualTo(5000.0);
    }

    @Test
    void doitFiltrerParType() {
        compteRepository.save(nouveauCompte(TypeCompte.COMPTE_COURANT, "MAD"));
        compteRepository.save(nouveauCompte(TypeCompte.COMPTE_EPARGNE, "EUR"));
        List<Compte> epargnes = compteRepository.findByType(TypeCompte.COMPTE_EPARGNE);
        assertThat(epargnes).isNotEmpty()
                .allMatch(c -> c.getType() == TypeCompte.COMPTE_EPARGNE);
    }

    @Test
    void doitSupprimerUnCompte() {
        Compte saved = compteRepository.save(nouveauCompte(TypeCompte.COMPTE_COURANT, "MAD"));
        compteRepository.deleteById(saved.getId());
        assertThat(compteRepository.findById(saved.getId())).isEmpty();
    }
}
