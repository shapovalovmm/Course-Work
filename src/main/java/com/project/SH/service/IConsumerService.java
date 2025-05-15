package com.project.SH.service;

import com.project.SH.model.impl.Admin;
import com.project.SH.model.impl.Consumer;
import com.project.SH.model.impl.User;

import java.util.List;

public interface IConsumerService {
    List<Consumer> findAllConsumers();
    Consumer saveConsumer(Consumer consumer);
    Consumer findByID(int userID);
    Consumer updateConsumer(Consumer consumer);
    void deleteConsumer(int userID);
}