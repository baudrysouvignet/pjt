package pharmacie.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Ligne{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id = null;

    @ManyToOne
    @JoinColumn(name="medicament_reference", nullable=false)
    private Medicament medicament;

    @ManyToOne
    @JoinColumn(name="commande_numero", nullable=false)
    private Commande commande;

    @NotBlank
    private Integer quantite;

}
