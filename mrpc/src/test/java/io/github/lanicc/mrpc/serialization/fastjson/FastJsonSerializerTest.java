package io.github.lanicc.mrpc.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
class FastJsonSerializerTest {

    @Test
    void write() throws IOException {
        FastJsonSerializer serializer = new FastJsonSerializer();
        ByteBufAllocator allocator = new UnpooledByteBufAllocator(true);
        ByteBuf buffer = allocator.buffer(10240);
        ByteBufOutputStream out = new ByteBufOutputStream(buffer);
        User u = new User("1 ", 12);
        serializer.write(u, out);
        out.flush();
        out.close();
        ByteBufInputStream in = new ByteBufInputStream(buffer);
        User user = serializer.read(User.class, in);
        Assertions.assertEquals(u.id, user.id);
        Assertions.assertEquals(u.age, user.age);
        System.out.println(JSON.toJSONString(Arrays.asList(new U(), new Object(), new User()), SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));

        String string = JSON.toJSONString(u);


    }

    static class U extends User {

    }
    static class User {

        private String id;

        private int age;

        public User() {
        }

        public User(String id, int age) {
            this.id = id;
            this.age = age;
        }

        public String getId() {
            return id;
        }

        public User setId(String id) {
            this.id = id;
            return this;
        }

        public int getAge() {
            return age;
        }

        public User setAge(int age) {
            this.age = age;
            return this;
        }
    }

}
