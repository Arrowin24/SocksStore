package ru.arrowin.socksstore.exceptions;

public class CreateFileException extends Exception {
    public CreateFileException(String message) {
        System.out.println("Ошибка создания файлов " + message);
    }
}
