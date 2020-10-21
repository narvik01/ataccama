package cz.strangeloop.ataccama.service;

import java.sql.SQLException;

public class CredentialsException extends RuntimeException {
    public CredentialsException(SQLException e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return "Invalid DB credentials.";
    }
}
