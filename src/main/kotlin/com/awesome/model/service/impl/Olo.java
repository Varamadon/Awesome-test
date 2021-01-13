package com.awesome.model.service.impl;

import com.awesome.model.Repo;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class Olo {
    private PriorityBlockingQueue<Repo> por() {
        return new PriorityBlockingQueue<>(10, Comparator.comparing(Repo::getName));
    }
}
