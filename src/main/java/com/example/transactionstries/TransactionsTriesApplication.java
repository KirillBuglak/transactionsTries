package com.example.transactionstries;

import com.example.transactionstries.services.JDBC.SomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionsTriesApplication implements CommandLineRunner {
//    @Autowired
//    private UnifyingService service;

    @Autowired
    private SomeService service;

    public static void main(String[] args) {
        SpringApplication.run(TransactionsTriesApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        try {
////            service.savePropagation();
//            service.readIsolated();
////            service.saveIsolated2();
////            service.singleTrx();
//        } catch (Exception e) {
//            System.err.println("Opps");
//        }
//        System.exit(777);
//    }
    @Override
    public void run(String... args) throws Exception {
//        service.dirtyRead();
//        service.repeatableRead();
        service.serializable();
        System.exit(777);
    }
}
