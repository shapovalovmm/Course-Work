package com.project.SH.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.project.SH.dto.TaleDto;
import com.project.SH.model.impl.Tale;
import com.project.SH.model.impl.User;
import com.project.SH.repository.TaleRepository;
import com.project.SH.repository.UserRepository;
import com.project.SH.service.impl.TaleService;
import com.project.SH.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/api/tales")
public class TaleController {

    @Autowired
    private TaleService taleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<TaleDto>> getAllTales() {
        List<Tale> tales = taleService.getAllTales();
        List<TaleDto> dtos = tales.stream()
                .map(taleService::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaleDto> getTaleById(@PathVariable Long id) {
        Tale tale = taleService.getTaleById(id);
        if (tale == null) {
            return ResponseEntity.notFound().build();
        }
        TaleDto dto = taleService.mapToDto(tale);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaleDto> createTale(@RequestBody Tale tale) {
        Tale saved = taleService.saveTale(tale);
        return ResponseEntity.ok(taleService.mapToDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaleDto> updateTale(@PathVariable Long id, @RequestBody Tale tale) {
        Tale updated = taleService.updateTale(id, tale);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taleService.mapToDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTale(@PathVariable Long id) {
        taleService.deleteTale(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("hasRole('CONSUMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> likeOrUnlikeTale(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        taleService.toggleLike(id, user);
        Tale updatedTale = taleService.getTaleById(id);
        int updatedLikes = updatedTale != null ? updatedTale.getLikes() : 0;

        boolean hasLiked = taleService.hasUserLikedTale(id, userDetails.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("likes", updatedLikes);
        response.put("hasLiked", hasLiked);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/like/status")
    @PreAuthorize("hasRole('CONSUMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> checkLikeStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        boolean hasLiked = taleService.hasUserLikedTale(id, userDetails.getUsername());
        Tale tale = taleService.getTaleById(id);
        int likes = tale != null ? tale.getLikes() : 0;

        Map<String, Object> response = new HashMap<>();
        response.put("hasLiked", hasLiked);
        response.put("likes", likes);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/favorite")
    @PreAuthorize("hasRole('CONSUMER') or hasRole('ADMIN')")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long id, Principal principal) {
        System.out.println("=== /favorite POST called ===");
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено"));

        taleService.toggleFavourite(id, user);
        return ResponseEntity.ok(Map.of("favorited", 1));
    }



    @GetMapping("/{id}/favorite/status")
    @PreAuthorize("hasRole('CONSUMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> isFavourite(@PathVariable Long id, Principal principal) {
        boolean isFavourite = taleService.hasFavourited(id, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("isFavourite", isFavourite);
        return ResponseEntity.ok(response);
    }
}
