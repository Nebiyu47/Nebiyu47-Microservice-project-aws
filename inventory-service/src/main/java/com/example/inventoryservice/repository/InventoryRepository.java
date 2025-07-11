package com.example.inventoryservice.repository;


import com.example.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findById(Long aLong);
    List<Inventory>findBySkuCodeIn(List<String> skuCode);
}
