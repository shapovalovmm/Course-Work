package com.project.SH.controller;

import com.project.SH.model.impl.Tale;
import com.project.SH.service.impl.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/{taleId}")
    @PreAuthorize("hasRole('CONSUMER')")
    public void addToFavorites(@PathVariable Long taleId, Principal principal) {
        favoriteService.addToFavorites(principal.getName(), taleId);
    }

    @GetMapping
    @PreAuthorize("hasRole('CONSUMER')")
    public List<Tale> getFavorites(Principal principal) {
        return favoriteService.getFavorites(principal.getName());
    }
}