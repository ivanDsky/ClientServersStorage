package ua.fastgroup.clientserversstorage.remote.result;

import lombok.*;

@Value
public class Success<T> extends Result<T> {
    T value;
}

