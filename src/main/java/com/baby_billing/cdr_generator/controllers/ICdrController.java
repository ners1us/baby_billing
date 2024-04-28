package com.baby_billing.cdr_generator.controllers;

import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ICdrController {

    ResponseEntity<String> generateAndSaveCdr() throws ExecutionException, InterruptedException, IOException;

    ResponseEntity<String> publishCdr();
}
