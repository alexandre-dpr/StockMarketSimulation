package bourse.repository;

import bourse.modele.StockTrendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTrendListRepository extends JpaRepository<StockTrendList, Integer> {
}
