package ru.arrowin.socksstore.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arrowin.socksstore.model.SocksOrder;
import ru.arrowin.socksstore.services.SocksService;

import javax.validation.Valid;


@RequestMapping("/api/socks")
@RestController
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping
    public ResponseEntity<String> createSocks(
            @Valid
            @RequestBody
            SocksOrder order)
    {
        try {
            socksService.addSocks(order);
            String message = socksService.messageOfResidual(order.getSocks());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }


    }

    @PutMapping
    public ResponseEntity<String> giveSocks(
            @Valid
            @RequestBody
            SocksOrder order)
    {
        try {
            socksService.deleteSocks(order);
            String message = socksService.messageOfResidual(order.getSocks());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<String> getSocksByParams(
            @RequestParam String color,
            @RequestParam int size,
            @RequestParam(
                    value = "cottonmin",
                    defaultValue = "0"
            ) int cottonMin,
            @RequestParam(
                    value = "cottonmax",
                    defaultValue = "100"
            ) int cottonMax)
    {

        return ResponseEntity.ok(cottonMax + " " + cottonMin);
    }

}
