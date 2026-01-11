package com.example.demoCRUD.Controller;

import com.example.demoCRUD.model.Item;
import com.example.demoCRUD.model.User;
import com.example.demoCRUD.repository.ItemRepository;
import com.example.demoCRUD.repository.UserRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemRepository repo;
    @Autowired
    private UserRepository userRepo;
    @GetMapping
    public List<Item> getMyTasks(Authentication auth) {
        String username = auth.getName();
        return repo.findByUserUsername(username);
    }

    @PostMapping
    public Item create(@RequestBody Item item, Authentication auth) {

        String username = auth.getName();
        User user = userRepo.findByUsername(username).get();

        item.setUser(user);

        return repo.save(item);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            Authentication auth) {

        System.out.println("HIT REAL UPDATE API");
        System.out.println("ID = " + id);
        System.out.println("BODY = " + payload);

        Item dbItem = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // OWNER CHECK (keep it)
        if (!dbItem.getUser().getUsername()
                .equals(auth.getName())) {
            return ResponseEntity.status(403)
                    .body("Not your task");
        }

        dbItem.setTitle(payload.get("title"));
        dbItem.setDescription(payload.get("description"));

        return ResponseEntity.ok(repo.save(dbItem));
    }




    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long id,
            Authentication auth) {

        Item item = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Security: only owner can delete
        if (!item.getUser().getUsername()
                .equals(auth.getName())) {
            return ResponseEntity
                    .status(403)
                    .body("Not your task");
        }

        repo.delete(item);
        return ResponseEntity.ok("Deleted");
    }

}
