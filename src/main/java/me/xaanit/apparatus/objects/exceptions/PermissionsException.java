package me.xaanit.apparatus.objects.exceptions;

/**
 * Created by Jacob on 5/15/2017.
 */
public class PermissionsException extends RuntimeException {

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException() {
        this("");
    }
}
