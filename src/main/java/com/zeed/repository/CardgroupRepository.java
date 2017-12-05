package com.zeed.repository;

import com.zeed.models.Cardgroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by longbridge on 12/5/17.
 */
@Repository
public interface CardgroupRepository extends PagingAndSortingRepository<Cardgroup,Long> {

}
