package com.bagguo.mydamai.cache;

import io.reactivex.functions.Function;

public class DiskOutFunction implements Function<String, String> {
    @Override
    public String apply(String s) throws Exception {
        return DiskCache.getInstance().get(s);
    }
}
