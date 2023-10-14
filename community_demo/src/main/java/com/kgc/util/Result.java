package com.kgc.util;

import java.util.HashMap;

/**
 * Created by jiang on 7/25/23 6:54 PM
 */
public class Result extends HashMap<String, Object> {


    public static Result ok(){
        Result result = new Result();
        result.put("code",200);
        result.put("msg","操作成功");
        return result;
    }

    public static Result error(String msg){
        Result result = new Result();
        result.put("code",500);
        result.put("msg",msg);
        return result;
    }

    /**
     * 添加自定义状态 如status
     * @param key
     * @param value
     * @return
     */
    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
