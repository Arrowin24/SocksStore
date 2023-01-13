package ru.arrowin.socksstore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.arrowin.socksstore.exceptions.ReadFileException;
import ru.arrowin.socksstore.services.FilesService;
import ru.arrowin.socksstore.services.SocksOrderService;
import ru.arrowin.socksstore.services.SocksService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FilesController {
    private final FilesService filesService;
    private final SocksService socksService;
    private final SocksOrderService orderService;

    public FilesController(FilesService filesService, SocksService socksService, SocksOrderService orderService) {
        this.filesService = filesService;
        this.socksService = socksService;
        this.orderService = orderService;
    }

    @Operation(
            summary = "Экспорт содержимого склада",
            description = "Предоставляется на скачивание файл, содержащий все данные о товаре, хранимом на складе в данный момент"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Файл с данными о содержимом склада успешно создан"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Не получилось создать файл..."
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
    @GetMapping("/export/socksStore")
    public ResponseEntity<InputStreamResource> downloadSocksStore() {
        File socksStoreFile = filesService.downloadSocksStoreFile();
        try {
            InputStreamResource streamResource = new InputStreamResource(new FileInputStream(socksStoreFile));
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                                 .contentLength(socksStoreFile.length())
                                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"socksStore.json\"")
                                 .body(streamResource);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(
            summary = "Перегрузка данных о складе",
            description = "Обновляются данные о содежимом склада при помощи передаваймого файла"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные успешно обновлены"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Не получилось обновить файл. Проверьте формат передаваймых данных"
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
    @PostMapping(value = "/import/socksStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadSocksStoreFile(@RequestParam MultipartFile file) throws ReadFileException {
        if (filesService.uploadSocksStoreFile(file)) {
            socksService.uploadSocksStoreFromFile();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(
            summary = "Экспорт истории операций",
            description = "Предоставляется на скачивание файл, содержащий все данные об операциях на складе"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Файл с данными успешно создан"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Не получилось создать файл..."
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
    @GetMapping("/export/socksOrders")
    public ResponseEntity<InputStreamResource> downloadOrdersStore() {
        File socksStoreFile = filesService.downloadOrdersFile();
        try {
            InputStreamResource streamResource = new InputStreamResource(new FileInputStream(socksStoreFile));
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                                 .contentLength(socksStoreFile.length())
                                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"orders.json\"")
                                 .body(streamResource);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Перегрузка истории операций",
            description = "Обновляются данные об операциях на складе при помощи передаваймого файла. Само содержимое склада не изменяется"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные успешно обновлены"
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Не получилось обновить данные. Проверте формат передаваймых операций."
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Непредвиденная ошибка сервера."
            )
            }
    )
    @PostMapping(value = "/import/socksOrders", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadOrdersFile(@RequestParam MultipartFile file) {
        if (filesService.uploadOrdersFile(file)) {
            orderService.uploadOrdersFromFile();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
