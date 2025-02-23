package com.hatla2y.backend.repositories;

import com.hatla2y.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByEmail(String email);
    User getUserByEmail(String email);
    Optional<User> getUserById(Integer id);
}
