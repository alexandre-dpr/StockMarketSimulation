package bourse.repository;

import bourse.modele.Ticker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TickerRepository extends JpaRepository<Ticker, String> {

    @Query("SELECT t FROM Ticker t WHERE t.ticker=:name OR t.Name LIKE %:name% ORDER BY LENGTH(t.ticker)")
    List<Ticker> findBySimilarName(@Param("name") String name);

    Page<Ticker> findAll(Pageable pageable);
}
