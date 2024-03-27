package ru.itcolleg.transaction.exception;

public class UnauthorizedTransactionException extends Exception{
    public UnauthorizedTransactionException(String message){
        super(message);
    }
}
