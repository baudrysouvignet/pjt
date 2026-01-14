package pharmacie.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Dispencaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer code = null;

    @NotBlank
    private String nom;

    @NotBlank
    private String contact;

     @NotBlank
    private String fonction;

    @NotBlank
    private String telephone;

    @NotBlank
    private String fax;


    @Embedded
    private AdressePostale adressePostale;
}
