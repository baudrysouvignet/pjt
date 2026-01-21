package pharmacie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacie.dao.CategorieRepository;
import pharmacie.entity.Categorie;

/**
 * Service pour gérer les opérations sur les catégories
 * avec validation des règles métier
 */
@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    /**
     * Supprime une catégorie seulement si elle n'a pas de médicaments
     * 
     * @param categorieId l'ID de la catégorie à supprimer
     * @throws IllegalArgumentException si la catégorie a des médicaments
     */
    public void deleteCategorieIfEmpty(Integer categorieId) {
        Categorie categorie = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!categorie.hasNOMedicaments()) {
            throw new IllegalArgumentException("Cannot delete a category that has medications");
        }

        categorieRepository.deleteById(categorieId);
    }

    /**
     * Supprime une catégorie seulement si elle n'a pas de médicaments
     * 
     * @param categorie la catégorie à supprimer
     * @throws IllegalArgumentException si la catégorie a des médicaments
     */
    public void deleteCategorie(Categorie categorie) {
        if (categorie != null && !categorie.hasNOMedicaments()) {
            throw new IllegalArgumentException("Cannot delete a category that has medications");
        }
        categorieRepository.delete(categorie);
    }
}
