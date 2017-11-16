package com.zeed.repository;

import com.zeed.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by longbridge on 11/13/17.
 */
@Repository
public interface UserRepositoy  extends PagingAndSortingRepository<User,Long>{
    User findByUsername(String username);
}
