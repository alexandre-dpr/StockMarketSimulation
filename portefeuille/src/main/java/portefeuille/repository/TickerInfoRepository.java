package portefeuille.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import portefeuille.modele.TickerInfo;

@Repository
public interface TickerInfoRepository extends JpaRepository<TickerInfo, String> {
}
