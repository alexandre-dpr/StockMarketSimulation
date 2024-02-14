package portefeuille.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portefeuille.modele.Mouvement;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, String> {
}
