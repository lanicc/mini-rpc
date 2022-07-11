package io.github.lanicc.mrpc.remote.proto;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class Response extends Protocol {

    @Override
    public String toString() {
        return "Response{" +
                "requestId=" + requestId +
                ", data=" + data +
                '}';
    }
}
