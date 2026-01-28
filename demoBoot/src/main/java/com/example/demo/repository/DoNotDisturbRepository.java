package com.example.demo.repository;

import com.example.demo.entity.DoNotDisturb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoNotDisturbRepository extends JpaRepository<DoNotDisturb, Long> {
    DoNotDisturb findByCustomerNo(String customerNo);
}
