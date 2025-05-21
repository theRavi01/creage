package com.creage.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.creage.model.Users;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{

//    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);

}
