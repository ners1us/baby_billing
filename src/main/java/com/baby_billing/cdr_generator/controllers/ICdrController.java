package com.baby_billing.cdr_generator.controllers;

import org.springframework.http.ResponseEntity;
import java.util.concurrent.ExecutionException;

public interface ICdrController {

    ResponseEntity<String> generateAndSaveCdr() throws ExecutionException, InterruptedException;

    ResponseEntity<String> publishCdr();
}
