package com.util;

import java.lang.reflect.Type;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 11:04
 */
public class ConvertTypeToken<T, D> {

    public Class<T> convertClassForEntity(Type type) {
        if (type instanceof Class) {
            return (Class<T>) type;
        }
        return null;
    }

    public Class<D> convertClassForDto(Type type) {
        if (type instanceof Class) {
            return (Class<D>) type;
        }
        return null;
    }

}
