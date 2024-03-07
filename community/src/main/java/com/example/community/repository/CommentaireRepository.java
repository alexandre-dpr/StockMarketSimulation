package com.example.community.repository;

import com.example.community.model.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire,Integer> {
    Optional<Commentaire> findById(Integer id);
    Optional<List<Commentaire>> findAllByAction(String action);

    @Query("SELECT c FROM Commentaire c WHERE c.action = :action ORDER BY c.date DESC")
    Optional<List<Commentaire>> findAllByActionOrderByDateDesc(@Param("action") String action);
}
