package com.zeed.repository;

import com.zeed.models.Cards;
import com.zeed.models.Status;
import com.zeed.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by longbridge on 11/22/17.
 */
@Repository
public interface CardsRepository extends JpaRepository<Cards,Long>{
    List<Cards> findCardsByUserOrderByUploadedOnDesc(User user);
    List<Cards> findCardsByStatus(Status status);
    @Query("select c from Cards c where c.status= :status")
    List<Cards> findCardsByStatusAndCount(Pageable pageable, @Param("status") Status status);
    @Query("select c from Cards c where c.status= :status order by id desc")
    List<Cards> findLastCardsByStatusAndCount(Pageable pageable, @Param("status") Status status);
    Cards findCardsById(Long id);
    List<Cards> findCardsByIdGreaterThanAndStatus(Long last,Status status);
    @Query("select max(id) from Cards where status='PENDING'")
    Long getLastPendingCard();
    @Query("select c from Cards c where status= :status and user=:user")
    List<Cards> getUserPendingCard(Pageable pageable,@Param("status") Status status,@Param("user") User user);
}
