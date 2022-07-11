package io.github.lanicc.mrpc.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.lanicc.mrpc.serialization.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class FastJsonSerializer implements Serializer {
    @Override
    public void write(Object o, OutputStream out) {
        try {
            JSON.writeJSONString(out, o, SerializerFeature.WriteClassName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T read(Class<T> clazz, InputStream in) {
        try {
            return JSON.parseObject(in, clazz, Feature.SupportAutoType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
