package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;

    }
    @GetMapping
    public ResponseEntity<List<Inventory>>getAllInventories(){
        return ResponseEntity.ok(inventoryRepository.findAll());
    }
    @PostMapping
    public ResponseEntity<Inventory>addInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryRepository.save(inventory));
    }
}
