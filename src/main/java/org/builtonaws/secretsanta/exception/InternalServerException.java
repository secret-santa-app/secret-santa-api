package org.builtonaws.secretsanta.exception;

public non-sealed class InternalServerException extends SecretSantaException {
    @Override
    public int getHttpStatusCode() {
        return 500;
    }

    public InternalServerException() {}

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(Throwable cause) {
        super(cause);
    }
}
