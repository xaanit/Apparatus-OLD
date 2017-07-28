package me.xaanit.apparatus.objects.marriage;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public class MultiHashMap<K, V> extends ConcurrentHashMap<K, V> {

    public MultiHashMap() {
        super();
    }

    public boolean put(V val, K... keys) {
        Object[] nullArr = new Object[keys.length];
        for (int i = 0; i < keys.length; i++)
            nullArr[i] = super.put(keys[i], val);
        return Arrays.stream(nullArr).filter(n -> n == null).count() == 0;
    }

    public boolean putIfAbsent(V val, K... keys) {
        Object[] nullArr = new Object[keys.length];
        for (int i = 0; i < keys.length; i++)
            nullArr[i] = super.putIfAbsent(keys[i], val);
        return Arrays.stream(nullArr).filter(n -> n == null).count() == 0;
    }
}
