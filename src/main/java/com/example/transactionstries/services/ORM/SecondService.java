package com.example.transactionstries.services.ORM;

import com.example.transactionstries.dao.entities.SomeEntity;
import com.example.transactionstries.dao.repos.ORM.SomeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class SecondService {
//    private final SomeRepository repository;
    @Transactional
    public void savePropagation(SomeEntity entity){
        log.info("Trying to save in {}", this.getClass().getName());
//        repository.save(entity);
        throw new RuntimeException("SecondServiceException");
    }
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    public void saveIsolated() throws InterruptedException {
        log.info("Trying to save in {}", this.getClass().getName());
//        SomeEntity someEntity = repository.findById(1L).orElseThrow();
//        someEntity.setName(someEntity.getName() + "second");
        Thread.sleep(5000);
//        repository.save(someEntity);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, readOnly = true)
    public SomeEntity getIsolated(){
        log.info("Trying to get in {}", this.getClass().getName());
//        SomeEntity someEntity = repository.findById(1L).orElseThrow();
//        someEntity.setName(someEntity.getName() + "Second");
//        return someEntity;
        return null;
    }
}
