package io.github.lanicc.mrpc.serialization;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public interface Serializer {

    void write(Object o, OutputStream out);

    <T> T read(Class<T> clazz, InputStream in);

}
