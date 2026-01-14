package pharmacie.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @ToString
public class AdressePostale{
    @NotBlank
    private String codePostal;

    @NotBlank
    private String ville;

    @NotBlank
    private String region;

    @NotBlank
    private String adresse;

    @NotBlank
    private String pays;
}
