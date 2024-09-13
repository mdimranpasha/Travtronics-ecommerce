package com.travtronicstech.assignment.repo;

import com.travtronicstech.assignment.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<Users,UUID> {


    Users findByUserName(String username);


}
