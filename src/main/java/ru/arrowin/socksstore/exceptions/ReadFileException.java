package ru.arrowin.socksstore.exceptions;

public class ReadFileException extends Exception{
    public ReadFileException(String message) {
        super("Reading file "+message+" error");
    }
}