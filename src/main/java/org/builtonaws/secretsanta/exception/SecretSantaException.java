package org.builtonaws.secretsanta.exception;

public abstract sealed class SecretSantaException extends Exception permits ClientException, InternalServerException {
    public SecretSantaException() {}

    public SecretSantaException(String message) {
        super(message);
    }

    public SecretSantaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecretSantaException(Throwable cause) {
        super(cause);
    }

    public abstract int getHttpStatusCode();
}
