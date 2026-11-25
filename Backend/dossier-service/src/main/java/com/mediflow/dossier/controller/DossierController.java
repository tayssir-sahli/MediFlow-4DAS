package com.mediflow.dossier.controller;

import com.mediflow.dossier.dto.request.AllergieRequest;
import com.mediflow.dossier.dto.request.AntecedentRequest;
import com.mediflow.dossier.dto.request.ConsultationRequest;
import com.mediflow.dossier.dto.request.CreerDossierRequest;
import com.mediflow.dossier.dto.request.DocumentRequest;
import com.mediflow.dossier.dto.request.GroupeSanguinRequest;
import com.mediflow.dossier.dto.response.AllergieResponse;
import com.mediflow.dossier.dto.response.AntecedentResponse;
import com.mediflow.dossier.dto.response.ConsultationResponse;
import com.mediflow.dossier.dto.response.DocumentResponse;
import com.mediflow.dossier.dto.response.DossierResponse;
import com.mediflow.dossier.exception.ApiError;
import com.mediflow.dossier.service.DossierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dossiers")
@RequiredArgsConstructor
@Tag(name = "Dossier médical",
        description = "Gestion centralisée du dossier médical d'un patient : allergies, antécédents, "
                + "consultations et documents")
public class DossierController {

    private final DossierService service;

    // ---------- Dossier ----------

    @PostMapping
    @Operation(summary = "Créer le dossier d'un patient",
            description = "Un seul dossier par patient. Rôle prévu : ADMIN ou système (inscription du patient).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dossier créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Un dossier existe déjà pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<DossierResponse> creerDossier(@Valid @RequestBody CreerDossierRequest request) {
        DossierResponse dossier = service.creerDossier(request);
        return ResponseEntity.created(URI.create("/dossiers/" + dossier.patientId())).body(dossier);
    }

    @GetMapping("/{patientId}")
    @Operation(summary = "Consulter un dossier",
            description = "Rôles prévus : PATIENT (le sien) ou MEDECIN (ayant un rendez-vous avec le patient).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dossier trouvé"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public DossierResponse getDossier(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId) {
        return service.getDossier(patientId);
    }

    @PatchMapping("/{patientId}/groupe-sanguin")
    @Operation(summary = "Modifier le groupe sanguin", description = "Rôle prévu : MEDECIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Groupe sanguin mis à jour"),
            @ApiResponse(responseCode = "400", description = "Groupe sanguin invalide",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public DossierResponse modifierGroupeSanguin(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId,
            @Valid @RequestBody GroupeSanguinRequest request) {
        return service.modifierGroupeSanguin(patientId, request);
    }

    @DeleteMapping("/{patientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un dossier", description = "Rôle prévu : ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dossier supprimé"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void supprimerDossier(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId) {
        service.supprimerDossier(patientId);
    }

    // ---------- Allergies ----------

    @GetMapping("/{patientId}/allergies")
    @Operation(summary = "Lister les allergies",
            description = "Rôles prévus : MEDECIN, PATIENT. Appelé aussi par le service Ordonnances avant "
                    + "de valider une ordonnance.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des allergies"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public List<AllergieResponse> getAllergies(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId) {
        return service.getAllergies(patientId);
    }

    @PostMapping("/{patientId}/allergies")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter une allergie", description = "Rôle prévu : MEDECIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Allergie ajoutée"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Allergie déjà enregistrée",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public AllergieResponse ajouterAllergie(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId,
            @Valid @RequestBody AllergieRequest request) {
        return service.ajouterAllergie(patientId, request);
    }

    @DeleteMapping("/{patientId}/allergies/{allergieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une allergie", description = "Rôle prévu : MEDECIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Allergie supprimée"),
            @ApiResponse(responseCode = "404", description = "Dossier ou allergie introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public void supprimerAllergie(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId,
            @Parameter(description = "Identifiant de l'allergie") @PathVariable String allergieId) {
        service.supprimerAllergie(patientId, allergieId);
    }

    // ---------- Antécédents ----------

    @GetMapping("/{patientId}/antecedents")
    @Operation(summary = "Lister les antécédents",
            description = "Rôles prévus : MEDECIN, PATIENT. Du plus récent au plus ancien.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des antécédents"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public List<AntecedentResponse> getAntecedents(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId) {
        return service.getAntecedents(patientId);
    }

    @PostMapping("/{patientId}/antecedents")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un antécédent", description = "Rôle prévu : MEDECIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Antécédent ajouté"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public AntecedentResponse ajouterAntecedent(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId,
            @Valid @RequestBody AntecedentRequest request) {
        return service.ajouterAntecedent(patientId, request);
    }

    // ---------- Consultations ----------

    @GetMapping("/{patientId}/consultations")
    @Operation(summary = "Lister les consultations",
            description = "Rôles prévus : MEDECIN, PATIENT. De la plus récente à la plus ancienne.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des consultations"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public List<ConsultationResponse> getConsultations(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId) {
        return service.getConsultations(patientId);
    }

    @PostMapping("/{patientId}/consultations")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter une consultation",
            description = "Rôle prévu : MEDECIN. Sera aussi alimentée par l'événement Kafka "
                    + "consultation.terminee.")
    @ApiResponse(responseCode = "201", description = "Consultation ajoutée")
    @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ConsultationResponse ajouterConsultation(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId,
            @Valid @RequestBody ConsultationRequest request) {
        return service.ajouterConsultation(patientId, request);
    }

    // ---------- Documents ----------

    @GetMapping("/{patientId}/documents")
    @Operation(summary = "Lister les documents", description = "Rôles prévus : MEDECIN, PATIENT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des documents"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public List<DocumentResponse> getDocuments(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId) {
        return service.getDocuments(patientId);
    }

    @PostMapping("/{patientId}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un document", description = "Rôles prévus : MEDECIN, PATIENT.")
    @ApiResponse(responseCode = "201", description = "Document ajouté")
    @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Aucun dossier pour ce patient",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    public DocumentResponse ajouterDocument(
            @Parameter(description = "Identifiant du patient", example = "12") @PathVariable Long patientId,
            @Valid @RequestBody DocumentRequest request) {
        return service.ajouterDocument(patientId, request);
    }
}