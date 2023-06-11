package com.example.transactionstries.services.JDBC;

import com.example.transactionstries.dao.repos.JDBC.SomeRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SomeService {
    private final SomeRepository repository;

    @PostConstruct
    public void init(){
        repository.saveInit();
    }

    public void dirtyRead(){
        repository.dirtyRead();
    }

    public void repeatableRead(){
        repository.repeatableRead();
    }

    public void serializable(){
        repository.serializable();
    }
}
