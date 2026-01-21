package pharmacie.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pharmacie.entity.Commande;
import pharmacie.entity.Medicament;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findBySaisieLeAfter(java.time.LocalDate date);

    /**
     * Calcul du nombre d'articles déjà commandés par un dispensaire
     * La commande doit avoir déjà été envoyée (envoyeeLe doit être renseigné)
     * 
     * @param dispensaireCode la clé du dispensaire
     * @return le nombre total d'articles commandés
     */
    @Query("SELECT SUM(l.quantite) FROM Commande c JOIN c.lignes l WHERE c.dispencaire.code = :dispensaireCode AND c.envoyeeLe IS NOT NULL")
    Long countArticlesOrderedByDispensaire(@Param("dispensaireCode") Integer dispensaireCode);

    /**
     * Trouver toutes les commandes en cours pour un dispensaire
     * Une commande est en cours si sa date d'envoi (envoyeeLe) n'est pas renseignée
     * 
     * @param dispensaireCode la clé du dispensaire
     * @return la liste des commandes en cours
     */
    @Query("SELECT c FROM Commande c WHERE c.dispencaire.code = :dispensaireCode AND c.envoyeeLe IS NULL")
    List<Commande> findPendingCommandsByDispensaire(@Param("dispensaireCode") Integer dispensaireCode);

}
