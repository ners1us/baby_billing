package com.baby_billing.brt.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface IBrtController {

    ResponseEntity<String> sendHistoryToHrs(@RequestParam Long historyId);
}
