package com.tpe.repository;

import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//--8----
public interface UserRepository extends JpaRepository<User, Long> {
    //--9
    //uniq olan userName
     Optional<User> findByUserName(String userName) throws ResourceNotFoundException;//findById

}
