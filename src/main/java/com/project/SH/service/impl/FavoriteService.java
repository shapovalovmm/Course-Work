package com.project.SH.service.impl;

import com.project.SH.model.impl.Favorite;
import com.project.SH.model.impl.Tale;
import com.project.SH.model.impl.User;
import com.project.SH.repository.FavoriteRepository;
import com.project.SH.repository.TaleRepository;
import com.project.SH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaleRepository taleRepository;

    public void addToFavorites(String username, Long taleId) {
        if (favoriteRepository.existsByUserUsernameAndTaleId(username, taleId)) {
            // Вже є в улюблених — можна кинути виняток або просто повернутися
            return;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tale tale = taleRepository.findById(taleId)
                .orElseThrow(() -> new RuntimeException("Tale not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setTale(tale);

        favoriteRepository.save(favorite);
    }

    public List<Tale> getFavorites(String username) {
        List<Favorite> favorites = favoriteRepository.findByUserUsername(username);
        return favorites.stream()
                .map(Favorite::getTale)
                .collect(Collectors.toList());
    }
}