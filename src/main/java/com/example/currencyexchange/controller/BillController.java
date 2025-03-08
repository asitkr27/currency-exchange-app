package com.example.currencyexchange.controller;

import com.example.currencyexchange.model.Bill;
import com.example.currencyexchange.service.BillService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BillController {
    private BillService billService;

    @PostMapping("/calculate")
    public ResponseEntity<Double> calculateBill(@RequestBody Bill bill) {
        double netAmount = billService.calculateNetAmount(bill);
        return ResponseEntity.ok(netAmount);
    }
}
