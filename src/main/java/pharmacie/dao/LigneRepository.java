package pharmacie.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Ligne;


public interface LigneRepository extends JpaRepository<Ligne, Integer> {

}
