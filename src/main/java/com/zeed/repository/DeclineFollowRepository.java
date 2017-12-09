package com.zeed.repository;

import com.zeed.models.DeclinedFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by longbridge on 12/8/17.
 */
@Repository
public interface DeclineFollowRepository extends JpaRepository<DeclinedFollow,Long> {


}
