package ru.otus.proxy;

import ru.otus.proxy.lazy.HeavyObject;
import ru.otus.proxy.lazy.HeavyObjectImpl;
import ru.otus.proxy.lazy.LazyProxy;
import ru.otus.proxy.security.SecurityAccessImpl;
import ru.otus.proxy.security.SecurityProxy;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class Demo {
    public static void main(String[] args) {
        //security();
        lazy();
    }

    private static void security() {
        var proxy = new SecurityProxy(new SecurityAccessImpl());
        proxy.access();
    }

    private static void lazy() {
        HeavyObject heavyObject = new HeavyObjectImpl();
        System.out.println(heavyObject);

        HeavyObject heavyObjectProxy = new LazyProxy(heavyObject);

        System.out.println(heavyObjectProxy.getValue());
        System.out.println(heavyObject);
        System.out.println(heavyObjectProxy.getValue());
    }
}
