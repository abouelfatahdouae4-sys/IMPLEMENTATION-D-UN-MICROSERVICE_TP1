package ma.emsi.ebankservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.emsi.ebankservice.dto.CompteRequestDTO;
import ma.emsi.ebankservice.dto.CompteResponseDTO;
import ma.emsi.ebankservice.services.CompteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 5. Web service Restful de gestion des comptes
 * (10. il délègue maintenant toute la logique à la couche service)
 */
@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@Tag(name = "Comptes", description = "Gestion des comptes bancaires")
public class CompteRestController {

    private final CompteService compteService;

    @GetMapping
    @Operation(summary = "Lister tous les comptes")
    public List<CompteResponseDTO> comptes() {
        return compteService.listerComptes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un compte par son id")
    public CompteResponseDTO compte(@PathVariable String id) {
        return compteService.consulterCompte(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un compte")
    public CompteResponseDTO save(@RequestBody CompteRequestDTO requestDTO) {
        return compteService.ajouterCompte(requestDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un compte")
    public CompteResponseDTO update(@PathVariable String id, @RequestBody CompteRequestDTO requestDTO) {
        return compteService.modifierCompte(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un compte")
    public void delete(@PathVariable String id) {
        compteService.supprimerCompte(id);
    }
}
