package org.builtonaws.secretsanta.exception;

public final class NotFoundException extends ClientException {
    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpStatusCode() {
        return 404;
    }
}
