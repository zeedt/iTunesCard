package com.zeed.repository;

import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by longbridge on 11/22/17.
 */
@Repository
public interface CardsRepository extends JpaRepository<Cards,Long>{
    List<Cards> findCardsByUserOrderByUploadedOnDesc(User user);
    List<Cards> findCardsByStatus(Status status);
    Cards findCardsById(Long id);
    List<Cards> findCardsByIdGreaterThan(Long last);
}
