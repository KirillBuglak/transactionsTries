package com.example.transactionstries.services.ORM;

import com.example.transactionstries.dao.entities.SomeEntity;
import com.example.transactionstries.dao.repos.ORM.SomeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class FirstService {
//    private final SomeRepository repository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePropagation(SomeEntity entity){
        log.info("Trying to save in {}", this.getClass().getName());
//        repository.save(entity);
    }
    @Transactional
    public void saveIsolated(){
        log.info("Trying to save in {}", this.getClass().getName());
//        SomeEntity someEntity = repository.findById(1L).orElseThrow();
//        someEntity.setName(someEntity.getName() + "first");
//        repository.save(someEntity);
    }
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    public SomeEntity getIsolated(){
        log.info("Trying to get in {}", this.getClass().getName());
//        SomeEntity someEntity = repository.findById(1L).orElseThrow();
//        someEntity.setName(someEntity.getName() + "First");
//        return someEntity;
        return null;
    }
}
