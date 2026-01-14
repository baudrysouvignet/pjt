package pharmacie.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;
import pharmacie.entity.Dispencaire;
import pharmacie.entity.Medicament;

public interface DispencaireRepository extends JpaRepository<Dispencaire, Integer> {

    List<Dispencaire> findByAdressePostaleRegion(String region);
}
