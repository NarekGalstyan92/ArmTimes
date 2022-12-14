package com.armtimes.armtimes.repository;


import com.armtimes.armtimes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String username);

    List<User> findTop10ByNameOrderByIdDesc(String name);
}
