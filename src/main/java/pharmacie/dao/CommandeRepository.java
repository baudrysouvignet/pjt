package pharmacie.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;
import pharmacie.entity.Medicament;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findBySaisieLeAfter(java.time.LocalDate date);
}
