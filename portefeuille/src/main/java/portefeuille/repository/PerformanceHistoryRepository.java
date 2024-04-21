package portefeuille.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portefeuille.modele.PerformanceHistory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceHistoryRepository extends JpaRepository<PerformanceHistory, Integer> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM PerformanceHistory p WHERE p.date > :date AND p.username=:username")
    boolean isPerformanceSavedSinceDate(@Param("date") LocalDateTime date, @Param("username") String username);

    List<PerformanceHistory> findPerformanceHistoryByUsername(String username);
}
