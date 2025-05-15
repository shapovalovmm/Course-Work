package com.project.SH.service.impl;

import com.project.SH.model.impl.User;
import com.project.SH.repository.UserRepository;
import com.project.SH.service.IGuestService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService implements IGuestService {
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository GuestRepository) {
        this.GuestRepository = GuestRepository;
    }

    @Override
    public List<Guest> findAllGuests() {
        return GuestRepository.findAll();
    }

    @Override
    public Guest saveGuest(Guest Guest) {
        return GuestRepository.save(Guest);
    }

    @Override
    public Guest findByID(int GuestID) {
        return GuestRepository.findById((long) GuestID).orElse(null);
    }

    @Override
    public Guest updateGuest(Guest Guest) {
        return GuestRepository.save(Guest);
    }

    @Override
    @Transactional
    public void deleteGuest(int GuestID) {
        GuestRepository.deleteById((long) GuestID);
    }
}
