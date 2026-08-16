package com.mam.serializer;

import com.mam.model.Task;

public interface TaskSerializer<T> {
    T serialize(Task task);
    Task deserialize(T data);
}
