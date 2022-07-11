package io.github.lanicc.mrpc.test.api;

import java.util.Date;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class User {

    private String id;

    private String name;

    private Date birth;

    public User() {
    }

    public User(String id, String name, Date birth) {
        this.id = id;
        this.name = name;
        this.birth = birth;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Date getBirth() {
        return birth;
    }

    public User setBirth(Date birth) {
        this.birth = birth;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                '}';
    }
}
