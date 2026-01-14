-- Données de base pour le projet Pharmacie
-- Dispensaire (Etablissements de santé qui passent commande de médicaments)
-- Le fichier est chargé au démarrage de l'application

-- Insertion des catégories de médicaments
INSERT INTO categorie (code, libelle, description) VALUES
(1, 'Antalgiques et Antipyrétiques', 'Médicaments contre la douleur et la fièvre'),
(2, 'Anti-inflammatoires', 'Médicaments réduisant l''inflammation'),
(3, 'Antibiotiques', 'Médicaments pour traiter les infections bactériennes'),
(4, 'Antihypertenseurs', 'Médicaments pour traiter l''hypertension artérielle'),
(5, 'Antidiabétiques', 'Médicaments pour traiter le diabète'),
(6, 'Antihistaminiques', 'Médicaments pour traiter les allergies'),
(7, 'Vitamines et Compléments', 'Suppléments nutritionnels'),
(8, 'Médicaments Cardiovasculaires', 'Médicaments pour le cœur et la circulation'),
(9, 'Médicaments Gastro-intestinaux', 'Médicaments pour les troubles digestifs'),
(10, 'Médicaments Respiratoires', 'Médicaments pour les troubles respiratoires');

-- Insertion des médicaments
INSERT INTO medicament (reference, nom, categorie_code, quantite_par_unite, prix_unitaire, unites_en_stock, unites_commandees, niveau_de_reappro, indisponible, imageurl) VALUES
(1, 'Morphine 10mg', 1, 'Boîte de 14 comprimés', 25.80, 80, 0, 15, false, 'https://images.unsplash.com/photo-1550572017-edd951aa8f72?w=400'),
(2, 'Doliprane Effervescent 1g', 1, 'Boîte de 8 comprimés', 3.50, 280, 0, 30, false, 'https://images.unsplash.com/photo-1587854692152-cbe660dbde88?w=400'),
(3, 'Efferalgan Vitamine C', 1, 'Boîte de 16 comprimés', 4.20, 220, 0, 25, false, 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=400'),
(4, 'Étodolac 400mg', 2, 'Boîte de 14 comprimés', 12.50, 110, 0, 15, false, 'https://images.unsplash.com/photo-1471864190281-a93a3070b6de?w=400'),
(5, 'Flurbiprofène 100mg', 2, 'Boîte de 30 comprimés', 10.80, 130, 0, 16, false, 'https://images.unsplash.com/photo-1550572017-edd951aa8f72?w=400'),
(6, 'Lévofloxacine 500mg', 3, 'Boîte de 7 comprimés', 15.80, 160, 0, 18, true, 'https://images.unsplash.com/photo-1628771065518-0d82f1938462?w=400'),
(7, 'Clindamycine 300mg', 3, 'Boîte de 16 gélules', 13.20, 140, 0, 16, true, 'https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=400');

-- Insertion des dispensaires
INSERT INTO dispencaire (code, nom, contact, fonction, telephone, fax, code_postal, ville, region, adresse, pays) VALUES
(1, 'Hôpital Central', 'Dr. Dupont', 'Directeur', '01-23-45-67-89', '01-23-45-67-90', '75001', 'Paris', 'Île-de-France', '1 Rue de la Santé', 'France'),
(2, 'Clinique Saint-Joseph', 'Mme. Martin', 'Responsable Achats', '02-34-56-78-90', '02-34-56-78-91', '69001', 'Lyon', 'Auvergne-Rhône-Alpes', '2 Avenue des Soins', 'France'),
(3, 'Centre Médical Nord', 'M. Durand', 'Pharmacien', '03-45-67-89-01', '03-45-67-89-02', '59000', 'Lille', 'Hauts-de-France', '3 Boulevard de la Médecine', 'France');

-- Insertion des commandes
INSERT INTO commande (numero, saisie_le, envoyee_le, montant_total, destinataire, remise, code_postal, ville, region, adresse, pays) VALUES
(1, '2023-01-15', '2023-01-20', 150.00, 'Hôpital Central', 10.00, '75001', 'Paris', 'Île-de-France', '1 Rue de la Santé', 'France'),
(2, '2023-02-10', '2023-02-15', 200.00, 'Clinique Saint-Joseph', 15.00, '69001', 'Lyon', 'Auvergne-Rhône-Alpes', '2 Avenue des Soins', 'France');

-- Insertion des lignes de commande
INSERT INTO ligne (id, medicament_reference, commande_numero, quantite) VALUES
(1, 1, 1, 5),
(2, 2, 1, 10),
(3, 4, 2, 8),
(4, 5, 2, 12);

