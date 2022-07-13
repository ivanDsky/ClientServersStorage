package ua.fastgroup.clientserversstorage.remote.result;

import lombok.Value;

@Value
public class Error<T> extends Result<T> {
    String message;
}
