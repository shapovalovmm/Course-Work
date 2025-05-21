package com.project.SH.service.impl;

import com.project.SH.dto.TaleDto;
import com.project.SH.model.impl.Tale;
import com.project.SH.model.impl.User;
import com.project.SH.repository.TaleRepository;
import com.project.SH.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class TaleService {
    @Autowired
    private TaleRepository taleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Tale> getAllTales() {
        return taleRepository.findAll();
    }

    public Tale getTaleById(Long id) {
        return taleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tale not found"));
    }

    public Tale saveTale(Tale tale) {
        return taleRepository.save(tale);
    }

    public Tale updateTale(Long id, Tale updatedTale) {
        Tale tale = getTaleById(id);
        tale.setTitle(updatedTale.getTitle());
        tale.setAuthor(updatedTale.getAuthor());
        tale.setContent(updatedTale.getContent());
        // Не оновлюємо лайки тут, щоб вони керувалися окремо
        return taleRepository.save(tale);
    }

    public void deleteTale(Long id) {
        taleRepository.deleteById(id);
    }

    @Transactional
    public void toggleLike(Long taleId, User user) {
        Tale tale = taleRepository.findById(taleId)
                .orElseThrow(() -> new RuntimeException("Казку не знайдено"));

        Set<User> likedUsers = tale.getLikedBy();

        boolean removed = false;
        for (User u : new HashSet<>(likedUsers)) { // ← копія, безпечна для обходу
            if (u.getId().equals(user.getId())) {
                likedUsers.remove(u);
                removed = true;
                break;
            }
        }
        if (!removed) {
            likedUsers.add(user);
        }

        taleRepository.save(tale);
    }
    public boolean hasUserLikedTale(Long taleId, String username) {
        Tale tale = getTaleById(taleId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено: " + username));

        return tale.getLikedBy().stream()
                .anyMatch(u -> u.getId().equals(user.getId()));
    }

    public TaleDto mapToDto(Tale tale) {
        TaleDto dto = new TaleDto();
        dto.setId(tale.getId());
        dto.setTitle(tale.getTitle());
        dto.setAuthor(tale.getAuthor());
        dto.setContent(tale.getContent());
        dto.setLikes(tale.getLikes());
        return dto;
    }

}