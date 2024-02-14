package portefeuille.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portefeuille.modele.Portefeuille;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille, String> {

//    @Query("SELECT p FROM Portefeuille p WHERE p.username=:username FINIR")
    public Portefeuille getPortefeuilleWithHistory(@Param("username") String username);
}
