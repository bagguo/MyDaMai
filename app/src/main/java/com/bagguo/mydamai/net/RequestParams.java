package com.bagguo.mydamai.net;

/**
 * Created by wanggang on 2017/3/14.
 * 请求参数
 */

public class RequestParams {

    private String baseUrl;
    private String path;
    private String params;

    public RequestParams(String baseUrl, String path, String params) {
        this.baseUrl = baseUrl;
        this.path = path;
        this.params = params;
    }
//发生、使形成url
    //string：字符序列不可变
    //stringbuffer：字符序列可改变，线程安全，效率低
    //stringbuilder：字符序列和变，线程不安全，效率高
    //url格式：url/path?参数1&2参数
    public String generateUrl() {
        return new StringBuffer()
                .append(baseUrl)
                .append(path)
                .append("?")
                .append(params)
                .toString();
    }
//静态内部类
    public static class Builder {
        private String baseUrl;
        private String path;
        private StringBuffer params;
//构造，触发stringbuffer类的参数
        public Builder() {
            params=new StringBuffer();
        }
//baseurl的方法
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            //baseurl不能以/结尾
            if (!baseUrl.endsWith("/")) {
                throw new RuntimeException("baseurl should end with / ");
            }
            return this;
        }
//path
        public Builder path(String path) {
            this.path = path;
            return this;
        }

//参数格式：key=value&
        public Builder params(String k, String v) {
            //& k=v & k=v & k=v
            this.params.append(k);
            this.params.append("=");
            this.params.append(v);
            this.params.append("&");
            return this;
        }

//
        public RequestParams build(){
            //去掉 &
            this.params.setLength(params.length()-1);
            return new RequestParams(baseUrl,path,params.toString());
        }
    }
}
