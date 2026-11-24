package ma.emsi.ebankservice.web;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebankservice.dto.CompteRequestDTO;
import ma.emsi.ebankservice.dto.CompteResponseDTO;
import ma.emsi.ebankservice.entities.Client;
import ma.emsi.ebankservice.entities.Compte;
import ma.emsi.ebankservice.exceptions.CompteNotFoundException;
import ma.emsi.ebankservice.repositories.ClientRepository;
import ma.emsi.ebankservice.repositories.CompteRepository;
import ma.emsi.ebankservice.services.CompteService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 11. Web service GraphQL du micro-service (schéma : resources/graphql/schema.graphqls)
 * Interface de test : http://localhost:8081/graphiql
 */
@Controller
@RequiredArgsConstructor
public class CompteGraphQLController {

    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final CompteService compteService;

    // ---------- Queries ----------
    @QueryMapping
    public List<Compte> comptesList() {
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument String id) {
        return compteRepository.findById(id)
                .orElseThrow(() -> new CompteNotFoundException(id));
    }

    @QueryMapping
    public List<Client> clientsList() {
        return clientRepository.findAll();
    }

    // Résolution du champ Client.comptes (évite les problèmes de chargement LAZY)
    @SchemaMapping(typeName = "Client", field = "comptes")
    public List<Compte> comptes(Client client) {
        return compteRepository.findByClientId(client.getId());
    }

    // ---------- Mutations ----------
    @MutationMapping
    public CompteResponseDTO addCompte(@Argument CompteRequestDTO compte) {
        return compteService.ajouterCompte(compte);
    }

    @MutationMapping
    public CompteResponseDTO updateCompte(@Argument String id, @Argument CompteRequestDTO compte) {
        return compteService.modifierCompte(id, compte);
    }

    @MutationMapping
    public Boolean deleteCompte(@Argument String id) {
        compteService.supprimerCompte(id);
        return true;
    }
}
