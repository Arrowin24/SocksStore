package ru.arrowin.socksstore.controllers;


import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arrowin.socksstore.model.SocksConsignment;
import ru.arrowin.socksstore.model.enums.Type;
import ru.arrowin.socksstore.services.SocksOrderService;
import ru.arrowin.socksstore.services.SocksService;

import javax.validation.Valid;

@Tag(
        name = "Операции над носками",
        description = "CRUD-операции над списком рецептов."
)
@RequestMapping("/api/socks")
@RestController
public class SocksController {

    private final SocksService socksService;
    private final SocksOrderService orderService;

    public SocksController(SocksService socksService, SocksOrderService orderService) {
        this.socksService = socksService;
        this.orderService = orderService;
    }

    @Operation(
            summary = "Прием товара на склад",
            description = "Добавление на склад носков в определенном количестве"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Носки успешно учтены на складе"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Один или несколько параметров товара отсутствуют или имею некорректный формат"
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
    @PostMapping
    public ResponseEntity<String> createSocks(
            @Valid
            @RequestBody
            SocksConsignment consignment)
    {
        try {
            socksService.addSocks(consignment);
            orderService.addOrder(consignment, Type.ADD);
            String message = socksService.messageOfResidual(consignment.getSocks());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Продажа товара",
            description = "Удаление товара со склада носков в определенном количестве"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Носки успешно удалены со складе"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Один или несколько параметров товара отсутствуют или имею некорректный формат"
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
    @PutMapping
    public ResponseEntity<String> giveSocks(
            @RequestBody
            @Valid SocksConsignment consignment)
    {
        try {
            socksService.deleteSocks(consignment);
            orderService.addOrder(consignment, Type.SELL);
            String message = socksService.messageOfResidual(consignment.getSocks());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Поиск товара",
            description = "Поиск товара по размеру, цвету или содержанию хлопка"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос удачный"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Один или несколько параметров поиска имею некорректный формат"
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
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
        try {
            int quantity = socksService.getSocksQuantityByParams(color, size, cottonMin, cottonMax);
            String message = "По выбранным параметрам сейчас на складе находятся: " + quantity + " шт. носок.";
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Списание товара",
            description = "Удаление товара со склада носков в определенном количестве"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Носки успешно удалены со складе"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Один или несколько параметров товара отсутствуют или имею некорректный формат"
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
    @DeleteMapping
    public ResponseEntity<String> deleteSocks(
            @RequestBody
            @Valid SocksConsignment consignment)
    {
        try {
            socksService.deleteSocks(consignment);
            orderService.addOrder(consignment, Type.DELETE);
            String message = socksService.messageOfResidual(consignment.getSocks());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
