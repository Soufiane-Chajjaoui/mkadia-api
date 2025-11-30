# Fonctionnement de l'algorithme ALS (Alternating Least Squares) pour le Système de Recommandation

Ce document explique le fonctionnement de la partie ALS (Alternating Least Squares) de votre système de recommandation pour votre présentation de projet.

## 1. Introduction à ALS

**ALS (Alternating Least Squares)** est un algorithme de filtrage collaboratif utilisé par Apache Spark MLlib. Son objectif est de prédire les préférences d'un utilisateur pour des produits qu'il n'a pas encore vus, en se basant sur ses interactions passées et celles d'autres utilisateurs similaires.

L'idée centrale est la **factorisation de matrice**.

## 2. Les Données d'Entrée (La Matrice Utilisateurs-Produits)

Le système commence par construire une grande matrice d'interactions :
*   **Lignes** : Les Utilisateurs ($U$)
*   **Colonnes** : Les Produits ($P$)
*   **Valeurs** : Le "score" ou la "note" d'interaction.

Dans votre projet `mkadia-api`, ces scores proviennent probablement de :
*   **Avis (Reviews)** : Notes explicites (ex: 1 à 5 étoiles) stockées dans la table `reviews`.
*   **Favoris (Favorites)** : Feedback implicite (ex: score de 1 si ajouté aux favoris).
*   **Commandes (Orders)** : Feedback implicite (ex: score basé sur l'achat).
*   **Analyse de Sentiment** : Comme mentionné dans votre projet, le score peut être ajusté par l'analyse sémantique des commentaires (ex: un commentaire très positif augmente le score).

| Utilisateur | iPhone 15 | Samsung S24 | MacBook Pro | ... |
| :--- | :---: | :---: | :---: | :---: |
| **Alice** | 5 | ? | 4 | ... |
| **Bob** | ? | 3 | ? | ... |
| **Charlie** | 2 | 5 | ? | ... |

*Le but est de remplir les "?" (valeurs manquantes).*

## 3. Factorisation de Matrice (Matrix Factorization)

ALS suppose que les notes sont le résultat du produit scalaire de deux vecteurs de caractéristiques cachées (facteurs latents) :
1.  **Vecteur Utilisateur ($u_i$)** : Représente les préférences de l'utilisateur (ex: aime la tech, préfère Apple, budget élevé...).
2.  **Vecteur Produit ($p_j$)** : Représente les caractéristiques du produit (ex: marque Apple, catégorie Laptop, prix...).

Mathématiquement, la note prédite $\hat{r}_{ij}$ pour l'utilisateur $i$ et le produit $j$ est :
$$ \hat{r}_{ij} = u_i \cdot p_j^T $$

## 4. L'Algorithme "Alternating Least Squares" (Moindres Carrés Alternés)

Le problème est de trouver les vecteurs $U$ et $P$ qui minimisent l'erreur entre les notes réelles et les notes prédites.
C'est difficile de trouver $U$ et $P$ en même temps. ALS résout cela en alternant :

1.  **Étape 1** : On fixe les vecteurs Produits ($P$) comme constants. On calcule les meilleurs vecteurs Utilisateurs ($U$) pour minimiser l'erreur (c'est un problème de moindres carrés facile à résoudre).
2.  **Étape 2** : On fixe les vecteurs Utilisateurs ($U$) comme constants. On calcule les meilleurs vecteurs Produits ($P$).
3.  **Répétition** : On alterne ces deux étapes jusqu'à ce que l'erreur ne diminue plus (convergence).

C'est cette alternance qui donne son nom à l'algorithme : **Alternating Least Squares**.

## 5. Génération des Recommandations

Une fois l'entraînement terminé, nous avons des vecteurs pour chaque utilisateur et chaque produit.

Pour recommander des produits à **Alice** :
1.  On prend le vecteur d'Alice ($u_{Alice}$).
2.  On calcule le produit scalaire avec **tous** les vecteurs de produits ($P$).
3.  On obtient une liste de scores prédits pour tous les produits.
4.  On trie ces scores du plus grand au plus petit.
5.  On filtre les produits qu'elle a déjà achetés.
6.  On retourne le Top N des produits (ex: les 5 meilleurs).

## 6. Architecture dans votre Projet

Dans le contexte de votre architecture technique :
1.  **Ingestion** : Les données (Reviews, Orders) sont extraites de PostgreSQL ou streamées via Kafka.
2.  **Traitement (Spark)** : Le job Spark lit ces données, construit la matrice et entraîne le modèle ALS.
3.  **Stockage** : Les vecteurs ou les recommandations pré-calculées sont sauvegardés (souvent dans Redis pour un accès rapide ou PostgreSQL).
4.  **API (Spring Boot)** : Lorsqu'Alice se connecte, l'API interroge la base de données (ou Redis) pour récupérer ses recommandations personnalisées et les affiche.

---
**En résumé pour la présentation :**
> "Nous utilisons l'algorithme ALS de Spark MLlib pour décomposer la matrice des interactions utilisateurs-produits en facteurs latents. En apprenant itérativement les préférences des utilisateurs et les caractéristiques des produits, nous pouvons prédire la pertinence d'un produit pour un utilisateur donné et ainsi proposer des recommandations personnalisées pertinentes."
