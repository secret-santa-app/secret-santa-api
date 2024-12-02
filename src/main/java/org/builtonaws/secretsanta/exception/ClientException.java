package org.builtonaws.secretsanta.exception;

public sealed class ClientException extends SecretSantaException permits BadRequestException, NotFoundException {
    @Override
    public int getHttpStatusCode() {
        return 400;
    }

    public ClientException() {}

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }
}
