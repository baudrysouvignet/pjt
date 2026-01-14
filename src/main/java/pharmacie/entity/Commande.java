package pharmacie.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Commande{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer numero = null;

    @NotBlank
    private Date saisieLe;

    @NotBlank
    private Date envoyeeLe;

    @NotBlank
    private BigDecimal montantTotal;

    @NotBlank
    private String destinataire;

    @NotBlank
    private BigDecimal remise;

    @Embedded
    private AdressePostale adresseLivraison;

}
