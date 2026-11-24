package ma.emsi.ebankservice;

import ma.emsi.ebankservice.entities.Client;
import ma.emsi.ebankservice.entities.Compte;
import ma.emsi.ebankservice.enums.TypeCompte;
import ma.emsi.ebankservice.repositories.ClientRepository;
import ma.emsi.ebankservice.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    /**
     * 4. Test de la couche DAO : insertion de données au démarrage puis affichage.
     */
    @Bean
    CommandLineRunner start(CompteRepository compteRepository, ClientRepository clientRepository) {
        return args -> {
            List.of("Ghita", "Yassine", "Salma", "Omar").forEach(nom ->
                    clientRepository.save(Client.builder().nom(nom).build()));

            clientRepository.findAll().forEach(client -> {
                for (int i = 0; i < 3; i++) {
                    Compte compte = Compte.builder()
                            .id(UUID.randomUUID().toString())
                            .dateCreation(new Date())
                            .solde(Math.round(1000 + Math.random() * 90000) * 1.0)
                            .devise(Math.random() > 0.5 ? "MAD" : "EUR")
                            .type(Math.random() > 0.5 ? TypeCompte.COMPTE_COURANT : TypeCompte.COMPTE_EPARGNE)
                            .client(client)
                            .build();
                    compteRepository.save(compte);
                }
            });

            System.out.println("========== Liste des comptes ==========");
            compteRepository.findAll().forEach(c -> System.out.println(
                    c.getId() + " | " + c.getType() + " | " + c.getSolde() + " " + c.getDevise()
                            + " | client : " + c.getClient().getNom()));
        };
    }
}
