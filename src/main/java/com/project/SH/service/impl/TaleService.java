package com.project.SH.service.impl;

import com.project.SH.dto.TaleDto;
import com.project.SH.model.impl.Tale;
import com.project.SH.model.impl.User;
import com.project.SH.repository.TaleRepository;
import com.project.SH.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        Set<Tale> likedTales = user.getLikedTales();

        boolean removed = false;
        for (User u : new HashSet<>(likedUsers)) { // копия для безопасного обхода
            if (u.getId().equals(user.getId())) {
                likedUsers.remove(u);
                likedTales.remove(tale);      // обязательно удалить из множества пользователя
                removed = true;
                tale.setLikes(tale.getLikes() - 1);  // уменьшить счетчик лайков
                break;
            }
        }
        if (!removed) {
            likedUsers.add(user);
            likedTales.add(tale);              // добавить в множество пользователя
            tale.setLikes(tale.getLikes() + 1);  // увеличить счетчик лайков
        }

        // Сохраняем обе стороны, чтобы изменения попали в БД
        userRepository.save(user);
        taleRepository.save(tale);
    }
    @Transactional
    public void toggleFavourite(Long taleId, User user) {
        System.out.println("=== TOGGLE FAVOURITE CALLED === Tale ID: " + taleId + ", User: " + user.getUsername());
        Tale tale = taleRepository.findById(taleId)
                .orElseThrow(() -> new RuntimeException("Казку не знайдено"));

        // Убедитесь, что обе стороны в контексте persistence
        // (или используйте метод merge)
        if (user.getFavouriteTales().contains(tale)) {
            user.getFavouriteTales().remove(tale);
        } else {
            user.getFavouriteTales().add(tale);
        }
        System.out.println("User " + user.getUsername() + " now has " + user.getFavouriteTales().size() + " favourites.");
        // Необходимо сохранить обе стороны связи при необходимости
        userRepository.save(user);
    }


    public boolean hasFavourited(Long taleId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Tale tale = taleRepository.findById(taleId)
                .orElseThrow(() -> new EntityNotFoundException("Tale not found"));
        return user.getFavouriteTales().contains(tale);
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