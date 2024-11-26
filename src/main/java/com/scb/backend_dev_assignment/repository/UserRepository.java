package com.scb.backend_dev_assignment.repository;

import com.scb.backend_dev_assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM \"users\" WHERE age = :age", nativeQuery = true)
    public List<User> findByAge(int age);
}
