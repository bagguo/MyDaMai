package com.bagguo.mydamai.cache;

import androidx.annotation.NonNull;
import io.reactivex.functions.Function;

public class DiskFunction implements Function<String, String>{

    @Override
    public String apply(@NonNull String s) throws Exception {
        String json = DiskCache.getInstance().get(s);
        return json;
    }
}
