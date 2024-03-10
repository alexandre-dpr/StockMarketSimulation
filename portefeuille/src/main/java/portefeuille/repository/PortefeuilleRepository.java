package portefeuille.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portefeuille.modele.Mouvement;
import portefeuille.modele.Portefeuille;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille, String> {

    @Query("SELECT p FROM Portefeuille p LEFT JOIN p.actions WHERE p.username=:username")
    Optional<Portefeuille> getPortefeuille(@Param("username") String username);

    @Query("SELECT p FROM Portefeuille p LEFT JOIN p.historique WHERE p.username=:username")
    Optional<Portefeuille> getHistorique(@Param("username") String username);

    /**
     * Si la personne possède une action donnée dans son portefeuille, on retourne le mouvement
     *
     * @param ticker ticker de l'action recherchée
     */
    @Query("SELECT m FROM Portefeuille p LEFT JOIN p.actions m WHERE p.username=:username AND m.ticker=:ticker")
    Optional<Mouvement> getActionPossedee(@Param("username") String username, @Param("ticker") String ticker);

    @Query(value = "SELECT count(*) FROM favoris WHERE username=:username", nativeQuery = true)
    int getNbFavoris(@Param("username") String username);

    @Query(value = "SELECT favori FROM favoris WHERE username=:username", nativeQuery = true)
    List<String> getFavoris(@Param("username") String username);
}
