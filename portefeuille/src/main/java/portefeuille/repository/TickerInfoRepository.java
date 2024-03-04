package portefeuille.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portefeuille.modele.TickerInfo;

import java.util.Optional;

@Repository
public interface TickerInfoRepository extends JpaRepository<TickerInfo, String> {
}
