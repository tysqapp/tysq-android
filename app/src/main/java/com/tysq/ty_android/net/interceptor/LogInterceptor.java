//package com.tysq.ty_android.net.interceptor;
//
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.tysq.ty_android.BuildConfig;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okio.Buffer;
//import okio.BufferedSource;
//
///**
// * author       : frog
// * time         : 2019/4/12 上午9:55
// * desc         : 日志拦截器
// * version      : 1.3.0
// */
//public class LogInterceptor implements Interceptor {
//
//    private static final String COLON = ": ";
//    private final static String TOP_LEFT_LINE = "┌";
//    private final static String MIDDLE_LINE = "├";
//    private final static String BOTTOM_LEFT_LINE = "└";
//    private final static String CRLF = "\r\n";
//    private final static String VERTICAL_LINE = "│";
//    private final static String HORIZONTAL_LINE = "─";
//
//    private final static boolean isShow = BuildConfig.BUILD_TYPE.equals("debug");
//
//    private static final Charset UTF8 = Charset.forName("UTF-8");
//
//    private final Random random = new Random();
//
//    @Override
//    public Response intercept(@NonNull Chain chain) throws IOException {
//
//        int requestId = random.nextInt(1000000);
//
//        Request request = chain.request();
//
//        buildRequestLog(requestId, request);
//
//        long reqTime = System.currentTimeMillis();
//        Response response = chain.proceed(request);
//
//        String url = request.url().uri().toString();
//        buildResponseLog(requestId, url, reqTime, response);
//
//        return response;
//
//    }
//
//    private void buildRequestLog(int requestId, Request request) {
//
//        if (!isShow) {
//            return;
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("HTTP Log").append(CRLF);
//        stringBuilder
//                .append(TOP_LEFT_LINE)
//                .append("------------request------------------------------------------")
//                .append(CRLF);
//        appendKeyAndValue(stringBuilder, "id", requestId + "");
//        appendKeyAndValue(stringBuilder, "url", request.url().uri().toString());
//        appendKeyAndValue(stringBuilder, "method", request.method());
//        stringBuilder.append(VERTICAL_LINE).append(CRLF);
//
//        stringBuilder.append(VERTICAL_LINE).append(" ").append("head params").append(CRLF);
//        Map<String, List<String>> headerMap = request.headers().toMultimap();
//        for (Map.Entry<String, List<String>> entry : headerMap.entrySet()) {
//            String key = entry.getKey();
//            for (String value : entry.getValue()) {
//                stringBuilder
//                        .append(VERTICAL_LINE)
//                        .append(" ")
//                        .append(MIDDLE_LINE)
//                        .append(key)
//                        .append(COLON)
//                        .append(value)
//                        .append(CRLF);
//            }
//        }
//
//        stringBuilder.append(VERTICAL_LINE).append(CRLF);
//
//        if (request.url().uri().toString().contains("api/file/originals")) {
//            stringBuilder
//                    .append(VERTICAL_LINE)
//                    .append(" ")
//                    .append("It's the download request.")
//                    .append(CRLF);
//        } else {
//            stringBuilder
//                    .append(VERTICAL_LINE)
//                    .append(" ")
//                    .append(request.body() == null ? "It's empty body" : bodyToString(request.body()))
//                    .append(CRLF);
//        }
//
//        stringBuilder
//                .append(BOTTOM_LEFT_LINE)
//                .append("-----------------------------------------------------------");
//
//        Log.i("LogInterceptor", stringBuilder.toString());
//    }
//
//    private void buildResponseLog(int requestId,
//                                  String url,
//                                  long reqTime,
//                                  Response response) {
//
//        if (!isShow) {
//            return;
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("HTTP Log").append(CRLF);
//        stringBuilder
//                .append(TOP_LEFT_LINE)
//                .append("------------response------------------------------------------")
//                .append(CRLF);
//        appendKeyAndValue(stringBuilder, "id", requestId + "");
//        appendKeyAndValue(stringBuilder, "time", System.currentTimeMillis() - reqTime + "");
//        appendKeyAndValue(stringBuilder, "url", url);
//        appendKeyAndValue(stringBuilder, "message", response.message());
//        appendKeyAndValue(stringBuilder, "code", response.code() + "");
//        stringBuilder.append(VERTICAL_LINE).append(CRLF);
//
//        stringBuilder.append(VERTICAL_LINE).append(" ").append("head params").append(CRLF);
//        Map<String, List<String>> headerMap = response.headers().toMultimap();
//
//        for (Map.Entry<String, List<String>> entry : headerMap.entrySet()) {
//            String key = entry.getKey();
//            for (String value : entry.getValue()) {
//                stringBuilder
//                        .append(VERTICAL_LINE)
//                        .append(" ")
//                        .append(MIDDLE_LINE)
//                        .append(key)
//                        .append(COLON)
//                        .append(value)
//                        .append(CRLF);
//            }
//        }
//
//        stringBuilder.append(VERTICAL_LINE).append(CRLF);
//        try {
//
//            String body;
//            if (response.body() == null || response.body().contentLength() == 0) {
//                body = "It's empty body";
//            } else {
//                BufferedSource source = response.body().source();
//                source.request(Long.MAX_VALUE);
//
//                body = source.buffer().clone().readString(UTF8);
//            }
//
//            stringBuilder
//                    .append(VERTICAL_LINE)
//                    .append(" ")
//                    .append(body)
//                    .append(CRLF);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        stringBuilder
//                .append(BOTTOM_LEFT_LINE)
//                .append("-----------------------------------------------------------");
//
//        Log.i("LogInterceptor", stringBuilder.toString());
//
//    }
//
//    private void appendKeyAndValue(StringBuilder stringBuilder, String key, String value) {
//        stringBuilder
//                .append(VERTICAL_LINE)
//                .append(" ")
//                .append(key)
//                .append(COLON)
//                .append(value)
//                .append(CRLF);
//    }
//
//    private String bodyToString(final RequestBody request) {
//        try {
//            final Buffer buffer = new Buffer();
//            if (request == null) {
//                return "";
//            }
//
//            request.writeTo(buffer);
//            return buffer.readUtf8();
//
//        } catch (final IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//}
