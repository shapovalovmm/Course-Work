package com.project.SH.service.impl;

import com.project.SH.model.impl.Admin;
import com.project.SH.model.impl.Consumer;
import com.project.SH.model.impl.User;
import com.project.SH.repository.AdminRepository;
import com.project.SH.repository.ConsumerRepository;
import com.project.SH.repository.UserRepository;
import com.project.SH.service.IAdminService;
import com.project.SH.service.IConsumerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerService implements IConsumerService {
    private final ConsumerRepository consumerRepository;

    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Override
    public List<Consumer> findAllConsumers() {
        return consumerRepository.findAll();
    }

    @Override
    public Consumer saveConsumer(Consumer consumer) {
        return consumerRepository.save(consumer);
    }

    @Override
    public Consumer findByID(int userID) {
        return consumerRepository.findById((long) userID).orElse(null);
    }

    @Override
    public Consumer updateConsumer(Consumer consumer) {
        return consumerRepository.save(consumer);
    }

    @Override
    @Transactional
    public void deleteConsumer(int userID) {
        consumerRepository.deleteById((long) userID);
    }
}
