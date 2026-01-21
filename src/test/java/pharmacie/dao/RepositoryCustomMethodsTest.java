package pharmacie.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pharmacie.entity.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RepositoryCustomMethodsTest {

    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private DispencaireRepository dispencaireRepository;
    @Autowired
    private LigneRepository ligneRepository;


    @Test // Ce test se base uniquement sur les données définies dans data.sql
    public void testMedicamentCustomMethods() {    
        Medicament indisponible = medicamentRepository.findByNom("Lévofloxacine 500mg").orElseThrow();
        Medicament disponible   = medicamentRepository.findByNom("Doliprane Effervescent 1g").orElseThrow();
    
        // Teste tous les médicaments disponibles
        List<Medicament> disponibles = medicamentRepository.findByIndisponibleFalse();

        assertTrue(disponibles.contains(disponible));
        assertFalse(disponibles.contains(indisponible));        
        assertFalse(disponibles.isEmpty());
    }

    @Test // Ce test crée les enregistrements nécessaires
    public void testCategorieCustomMethods() {
        Categorie c1 = new Categorie();
        c1.setLibelle("AnalgesiquesTest");
        categorieRepository.save(c1);

        Categorie c2 = new Categorie();
        c2.setLibelle("AntibiotiquesTest");
        categorieRepository.save(c2);

        // findByLibelle
        Categorie found = categorieRepository.findByLibelle("AnalgesiquesTest");
        assertNotNull(found);
        assertEquals("AnalgesiquesTest", found.getLibelle());

        // findByLibelleContaining
        List<Categorie> list = categorieRepository.findByLibelleContaining("iquesTest");
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AntibiotiquesTest")));
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AnalgesiquesTest")));
    }

    // ==================== TESTS DES CONTRAINTES D'INTÉGRITÉ ====================

    @Test
    public void testMedicamentMustHaveCategory() {
        // Créer une catégorie
        Categorie categorie = new Categorie("Analgésiques");
        categorieRepository.save(categorie);

        // Créer un médicament avec une catégorie (succès)
        Medicament med = new Medicament("Paracétamol");
        med.setCategorie(categorie);
        medicamentRepository.save(med);
        
        assertTrue(medicamentRepository.findByNom("Paracétamol").isPresent());
        assertEquals(categorie, medicamentRepository.findByNom("Paracétamol").get().getCategorie());
    }

    @Test
    public void testCanDeleteCategoryWithoutMedicaments() {
        // Créer une catégorie sans médicaments
        Categorie categorie = new Categorie("CategorieEmpty");
        categorieRepository.save(categorie);
        Integer categoryId = categorie.getCode();

        // Vérifier qu'on peut la supprimer
        assertDoesNotThrow(() -> {
            categorieRepository.deleteById(categoryId);
        });

        // Vérifier qu'elle est supprimée
        assertFalse(categorieRepository.findById(categoryId).isPresent());
    }

    @Test
    public void testCannotDeleteCategoryWithMedicaments() {
        // Créer une catégorie avec un médicament
        Categorie categorie = new Categorie("CategorieWithMeds");
        Medicament med = new Medicament("Ibuprofène");
        med.setCategorie(categorie);
        categorie.getMedicaments().add(med);
        categorieRepository.save(categorie);
        Integer categoryId = categorie.getCode();

        // Vérifier qu'on ne peut pas la supprimer
        assertThrows(IllegalArgumentException.class, () -> {
            categorieRepository.deleteById(categoryId);
        });

        // Vérifier qu'elle existe toujours
        assertTrue(categorieRepository.findById(categoryId).isPresent());
    }

    @Test
    public void testDeleteCommandDeletesLines() {
        // Créer un dispensaire
        Dispencaire dispencaire = new Dispencaire();
        dispencaire.setNom("Dispensaire Test");
        dispencaire.setContact("Contact");
        dispencaire.setFonction("Fonction");
        dispencaire.setTelephone("123456");
        dispencaire.setFax("654321");
        dispencaireRepository.save(dispencaire);

        // Créer une commande
        Commande commande = new Commande();
        commande.setSaisieLe(Date.valueOf(LocalDate.now()));
        commande.setMontantTotal(BigDecimal.TEN);
        commande.setDestinataire("Destinataire");
        commande.setRemise(BigDecimal.ZERO);
        commande.setDispencaire(dispencaire);
        commandeRepository.save(commande);
        Integer commandeId = commande.getNumero();

        // Créer une ligne de commande
        Ligne ligne = new Ligne();
        Categorie categorie = new Categorie("TestCat");
        Medicament med = new Medicament("Aspirine");
        med.setCategorie(categorie);
        categorieRepository.save(categorie);
        medicamentRepository.save(med);

        ligne.setMedicament(med);
        ligne.setCommande(commande);
        ligne.setQuantite(5);
        ligneRepository.save(ligne);
        Integer ligneId = ligne.getId();

        // Vérifier que la ligne existe
        assertTrue(ligneRepository.findById(ligneId).isPresent());

        // Supprimer la commande
        commandeRepository.deleteById(commandeId);

        // Vérifier que la commande est supprimée
        assertFalse(commandeRepository.findById(commandeId).isPresent());

        // Vérifier que la ligne est aussi supprimée (cascade)
        assertFalse(ligneRepository.findById(ligneId).isPresent());
    }

    @Test
    public void testDeleteDispensaireDeletesCommands() {
        // Créer un dispensaire
        Dispencaire dispencaire = new Dispencaire();
        dispencaire.setNom("Dispensaire Test Delete");
        dispencaire.setContact("Contact");
        dispencaire.setFonction("Fonction");
        dispencaire.setTelephone("123456");
        dispencaire.setFax("654321");
        dispencaireRepository.save(dispencaire);
        Integer dispensaireId = dispencaire.getCode();

        // Créer des commandes
        Commande commande1 = new Commande();
        commande1.setSaisieLe(Date.valueOf(LocalDate.now()));
        commande1.setMontantTotal(BigDecimal.TEN);
        commande1.setDestinataire("Destinataire1");
        commande1.setRemise(BigDecimal.ZERO);
        commande1.setDispencaire(dispencaire);
        commandeRepository.save(commande1);

        Commande commande2 = new Commande();
        commande2.setSaisieLe(Date.valueOf(LocalDate.now()));
        commande2.setMontantTotal(BigDecimal.TEN);
        commande2.setDestinataire("Destinataire2");
        commande2.setRemise(BigDecimal.ZERO);
        commande2.setDispencaire(dispencaire);
        commandeRepository.save(commande2);

        Integer commande1Id = commande1.getNumero();
        Integer commande2Id = commande2.getNumero();

        // Vérifier que les commandes existent
        assertTrue(commandeRepository.findById(commande1Id).isPresent());
        assertTrue(commandeRepository.findById(commande2Id).isPresent());

        // Supprimer le dispensaire
        dispencaireRepository.deleteById(dispensaireId);

        // Vérifier que le dispensaire est supprimé
        assertFalse(dispencaireRepository.findById(dispensaireId).isPresent());

        // Vérifier que les commandes sont aussi supprimées (cascade)
        assertFalse(commandeRepository.findById(commande1Id).isPresent());
        assertFalse(commandeRepository.findById(commande2Id).isPresent());
    }

    // ==================== TESTS DES REQUÊTES ====================

    @Test
    public void testCountArticlesOrderedByDispensaire() {
        // Créer un dispensaire
        Dispencaire dispencaire = new Dispencaire();
        dispencaire.setNom("Dispensaire Count");
        dispencaire.setContact("Contact");
        dispencaire.setFonction("Fonction");
        dispencaire.setTelephone("123456");
        dispencaire.setFax("654321");
        dispencaireRepository.save(dispencaire);

        // Créer une catégorie et des médicaments
        Categorie categorie = new Categorie("TestCategory");
        Medicament med1 = new Medicament("Med1");
        med1.setCategorie(categorie);
        Medicament med2 = new Medicament("Med2");
        med2.setCategorie(categorie);
        categorieRepository.save(categorie);
        medicamentRepository.save(med1);
        medicamentRepository.save(med2);

        // Créer une commande ENVOYÉE
        Commande commande1 = new Commande();
        commande1.setSaisieLe(Date.valueOf(LocalDate.now().minusDays(5)));
        commande1.setEnvoyeeLe(Date.valueOf(LocalDate.now()));  // ENVOYÉE
        commande1.setMontantTotal(BigDecimal.TEN);
        commande1.setDestinataire("Dest1");
        commande1.setRemise(BigDecimal.ZERO);
        commande1.setDispencaire(dispencaire);
        commandeRepository.save(commande1);

        // Créer une commande NOT ENVOYÉE
        Commande commande2 = new Commande();
        commande2.setSaisieLe(Date.valueOf(LocalDate.now()));
        commande2.setEnvoyeeLe(null);  // NOT ENVOYÉE
        commande2.setMontantTotal(BigDecimal.TEN);
        commande2.setDestinataire("Dest2");
        commande2.setRemise(BigDecimal.ZERO);
        commande2.setDispencaire(dispencaire);
        commandeRepository.save(commande2);

        // Ajouter des lignes
        Ligne ligne1 = new Ligne();
        ligne1.setMedicament(med1);
        ligne1.setCommande(commande1);
        ligne1.setQuantite(5);
        ligneRepository.save(ligne1);

        Ligne ligne2 = new Ligne();
        ligne2.setMedicament(med2);
        ligne2.setCommande(commande1);
        ligne2.setQuantite(3);
        ligneRepository.save(ligne2);

        Ligne ligne3 = new Ligne();
        ligne3.setMedicament(med1);
        ligne3.setCommande(commande2);
        ligne3.setQuantite(10);
        ligneRepository.save(ligne3);

        // Tester la requête: seule la commande1 (envoyée) doit être comptée
        Long count = commandeRepository.countArticlesOrderedByDispensaire(dispencaire.getCode());
        assertEquals(8L, count); // 5 + 3, pas le 10 de commande2
    }

    @Test
    public void testFindPendingCommandsByDispensaire() {
        // Créer un dispensaire
        Dispencaire dispencaire = new Dispencaire();
        dispencaire.setNom("Dispensaire Pending");
        dispencaire.setContact("Contact");
        dispencaire.setFonction("Fonction");
        dispencaire.setTelephone("123456");
        dispencaire.setFax("654321");
        dispencaireRepository.save(dispencaire);

        // Créer une commande ENVOYÉE
        Commande commande1 = new Commande();
        commande1.setSaisieLe(Date.valueOf(LocalDate.now().minusDays(5)));
        commande1.setEnvoyeeLe(Date.valueOf(LocalDate.now()));
        commande1.setMontantTotal(BigDecimal.TEN);
        commande1.setDestinataire("Dest1");
        commande1.setRemise(BigDecimal.ZERO);
        commande1.setDispencaire(dispencaire);
        commandeRepository.save(commande1);

        // Créer des commandes EN COURS (envoyeeLe = null)
        Commande commande2 = new Commande();
        commande2.setSaisieLe(Date.valueOf(LocalDate.now()));
        commande2.setEnvoyeeLe(null);
        commande2.setMontantTotal(BigDecimal.TEN);
        commande2.setDestinataire("Dest2");
        commande2.setRemise(BigDecimal.ZERO);
        commande2.setDispencaire(dispencaire);
        commandeRepository.save(commande2);

        Commande commande3 = new Commande();
        commande3.setSaisieLe(Date.valueOf(LocalDate.now().minusDays(1)));
        commande3.setEnvoyeeLe(null);
        commande3.setMontantTotal(BigDecimal.TEN);
        commande3.setDestinataire("Dest3");
        commande3.setRemise(BigDecimal.ZERO);
        commande3.setDispencaire(dispencaire);
        commandeRepository.save(commande3);

        // Tester la requête
        List<Commande> pendingCommands = commandeRepository.findPendingCommandsByDispensaire(dispencaire.getCode());
        assertEquals(2, pendingCommands.size());
        assertTrue(pendingCommands.contains(commande2));
        assertTrue(pendingCommands.contains(commande3));
        assertFalse(pendingCommands.contains(commande1));
    }

    @Test
    public void testFindAvailableForOrderByCategory() {
        // Créer une catégorie
        Categorie categorie = new Categorie("CatAvailable");
        categorieRepository.save(categorie);

        // Créer des médicaments avec différents états
        // Med1: disponible, stock >= commande
        Medicament med1 = new Medicament("Med1Available");
        med1.setCategorie(categorie);
        med1.setIndisponible(false);
        med1.setUnitesEnStock(20);
        med1.setUnitesCommandees(5);
        medicamentRepository.save(med1);

        // Med2: indisponible
        Medicament med2 = new Medicament("Med2Unavailable");
        med2.setCategorie(categorie);
        med2.setIndisponible(true);
        med2.setUnitesEnStock(20);
        med2.setUnitesCommandees(5);
        medicamentRepository.save(med2);

        // Med3: disponible mais stock < commande
        Medicament med3 = new Medicament("Med3LowStock");
        med3.setCategorie(categorie);
        med3.setIndisponible(false);
        med3.setUnitesEnStock(3);
        med3.setUnitesCommandees(5);
        medicamentRepository.save(med3);

        // Med4: disponible, stock >= commande
        Medicament med4 = new Medicament("Med4Available");
        med4.setCategorie(categorie);
        med4.setIndisponible(false);
        med4.setUnitesEnStock(10);
        med4.setUnitesCommandees(10);  // exact égalité
        medicamentRepository.save(med4);

        // Med5: en autre catégorie
        Categorie categorie2 = new Categorie("CatOther");
        categorieRepository.save(categorie2);
        Medicament med5 = new Medicament("Med5OtherCategory");
        med5.setCategorie(categorie2);
        med5.setIndisponible(false);
        med5.setUnitesEnStock(20);
        med5.setUnitesCommandees(5);
        medicamentRepository.save(med5);

        // Tester la requête
        List<Medicament> available = medicamentRepository.findAvailableForOrderByCategory(categorie.getCode());
        assertEquals(2, available.size());
        assertTrue(available.contains(med1));
        assertTrue(available.contains(med4));
        assertFalse(available.contains(med2));  // indisponible
        assertFalse(available.contains(med3));  // stock insuffisant
        assertFalse(available.contains(med5));  // autre catégorie
    }

}
