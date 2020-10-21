package cz.strangeloop.ataccama.service;

import java.sql.SQLException;

public class UnknownDBException extends RuntimeException {
    public UnknownDBException(Throwable e) {
        super(e);
    }
}
