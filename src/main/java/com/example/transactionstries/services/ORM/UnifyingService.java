package com.example.transactionstries.services.ORM;

import com.example.transactionstries.dao.entities.SomeEntity;
import com.example.transactionstries.dao.repos.ORM.SomeRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UnifyingService {
    private final FirstService firstS;
    private final SecondService secondS;
//    private final SomeRepository repository;
    private SomeEntity firstE;
    private SomeEntity secondE;

    public UnifyingService(FirstService firstS, SecondService secondS
//            , SomeRepository repository
    ) {
        this.firstS = firstS;
        this.secondS = secondS;
//        this.repository = repository;
    }

    @PostConstruct
    public void init() {
//        repository.save(new SomeEntity(1L, "AAA"));
        firstE = new SomeEntity(111, "First");
        secondE = new SomeEntity(222, "Second");
    }

    @Transactional
    public void savePropagation() {
        log.info("Trying to save in {}", this.getClass().getName());
        firstS.savePropagation(firstE);
        secondS.savePropagation(secondE);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public void readIsolated(){
        log.info("Trying to save in {}", this.getClass().getName());
//        String nameBefore = repository.findById(1L).orElseThrow().getName();
//        System.err.println("Before - " + nameBefore);
        firstS.saveIsolated();
//        System.err.println("Before - " + nameBefore);
//        System.err.println("After - " + repository.findById(1L).orElseThrow().getName());
    }

    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public void saveIsolated2() {
        SomeEntity firstSIsolated = firstS.getIsolated();
        SomeEntity secondSIsolated = secondS.getIsolated();
//        repository.save(secondSIsolated);
//        repository.save(firstSIsolated);
//        System.err.println(repository.findById(1L).orElseThrow().getName());
    }
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public void singleTrx(){
        log.info("Trying to get in {}", this.getClass().getName());
//        SomeEntity someEntity = repository.findById(1L).orElseThrow();
//        someEntity.setName(someEntity.getName() + "uni");
//        System.out.println("Enter anything to continue");
//        new java.util.Scanner(System.in).nextLine();
//        repository.save(someEntity);
    }
}
