package com.dheeru.apps.twitter.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by dkthaku on 6/8/16.
 */
public class SafeMapAdapter implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> tokenType) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, tokenType);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader arg0) throws IOException {
                final T t = delegate.read(arg0);
                if (Map.class.isAssignableFrom(tokenType.getRawType())) {
                    if (t == null) {
                        return (T) Collections.EMPTY_MAP;
                    }

                    final Map<?, ?> map = (Map<?, ?>) t;
                    return (T) Collections.unmodifiableMap(map);
                }
                return t;
            }
        };
    }
}
