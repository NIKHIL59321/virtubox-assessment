package com.example.demoCRUD.repository;

import com.example.demoCRUD.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserUsername(String username);

}
