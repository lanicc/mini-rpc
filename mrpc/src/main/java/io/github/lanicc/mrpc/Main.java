package io.github.lanicc.mrpc;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created on ${DATE}.
 *
 * @author lan
 */
public class Main {
    interface UserApi {

        void add(Map<String, String> userAttrs);

        List<Map<String, String>> listUser();

    }

    public static void main(String[] args) {
        UserApi userApi =
                proxy(UserApi.class, new MethodInterceptor() {
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                System.out.println(method.getName());
                System.out.println(method.getDeclaringClass());
                return null;
            }
        });
        userApi.add(Collections.emptyMap());
        userApi.listUser();
        userApi.toString();
        userApi.hashCode();
    }

    @SuppressWarnings("unchecked")
    public static <T> T proxy(Class<T> inf, MethodInterceptor mi) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{UserApi.class});
        enhancer.setCallback(mi);
        return (T) enhancer.create();
    }
}
