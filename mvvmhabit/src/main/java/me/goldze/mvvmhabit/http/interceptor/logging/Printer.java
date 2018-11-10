package me.goldze.mvvmhabit.http.interceptor.logging;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okio.Buffer;

/**
 * @author ihsan on 09/02/2017.
 */

class Printer {

    private static final int JSON_INDENT = 3;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;

    private static final String[] OMITTED_RESPONSE = {LINE_SEPARATOR, "Omitted response body"};
    private static final String[] OMITTED_REQUEST = {LINE_SEPARATOR, "Omitted request body"};

    private static final String N = "\n";
    private static final String T = "\t";
    private static final String REQUEST_UP_LINE = "┌────── Request ────────────────────────────────────────────────────────────────────────";
    private static final String END_LINE = "└───────────────────────────────────────────────────────────────────────────────────────";
    private static final String RESPONSE_UP_LINE = "┌────── Response ───────────────────────────────────────────────────────────────────────";
    private static final String BODY_TAG = "Body:";
    private static final String URL_TAG = "URL: ";
    private static final String METHOD_TAG = "Method: @";
    private static final String HEADERS_TAG = "Headers:";
    private static final String STATUS_CODE_TAG = "Status Code: ";
    private static final String RECEIVED_TAG = "Received in: ";
    private static final String CORNER_UP = "┌ ";
    private static final String CORNER_BOTTOM = "└ ";
    private static final String CENTER_LINE = "├ ";
    private static final String DEFAULT_LINE = "│ ";

    protected Printer() {
        throw new UnsupportedOperationException();
    }

    private static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || N.equals(line) || T.equals(line) || TextUtils.isEmpty(line.trim());
    }

    static void printJsonRequest(LoggingInterceptor.Builder builder, Request request) {
        String requestBody = LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + bodyToString(request);
        String tag = builder.getTag(true);
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, REQUEST_UP_LINE);
        logLines(builder.getType(), tag, new String[]{URL_TAG + request.url()}, builder.getLogger(), false);
        logLines(builder.getType(), tag, getRequest(request, builder.getLevel()), builder.getLogger(), true);
        if (request.body() instanceof FormBody) {
            StringBuilder formBody = new StringBuilder();
            FormBody body = (FormBody) request.body();
            if (body != null && body.size() != 0) {
                for (int i = 0; i < body.size(); i++) {
                    formBody.append(body.encodedName(i) + "=" + body.encodedValue(i) + "&");
                }
                formBody.delete(formBody.length() - 1, formBody.length());
                logLines(builder.getType(), tag, new String[]{formBody.toString()}, builder.getLogger(), true);
            }
        }
        if (builder.getLevel() == Level.BASIC || builder.getLevel() == Level.BODY) {
            logLines(builder.getType(), tag, requestBody.split(LINE_SEPARATOR), builder.getLogger(), true);
        }
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, END_LINE);
    }

    static void printJsonResponse(LoggingInterceptor.Builder builder, long chainMs, boolean isSuccessful,
                                  int code, String headers, String bodyString, List<String> segments) {
        String responseBody = LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + getJsonString(bodyString);
        String tag = builder.getTag(false);
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, RESPONSE_UP_LINE);

        logLines(builder.getType(), tag, getResponse(headers, chainMs, code, isSuccessful,
                builder.getLevel(), segments), builder.getLogger(), true);
        if (builder.getLevel() == Level.BASIC || builder.getLevel() == Level.BODY) {
            logLines(builder.getType(), tag, responseBody.split(LINE_SEPARATOR), builder.getLogger(), true);
        }
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, END_LINE);
    }

    static void printFileRequest(LoggingInterceptor.Builder builder, Request request) {
        String tag = builder.getTag(true);
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, REQUEST_UP_LINE);
        logLines(builder.getType(), tag, new String[]{URL_TAG + request.url()}, builder.getLogger(), false);
        logLines(builder.getType(), tag, getRequest(request, builder.getLevel()), builder.getLogger(), true);
        if (request.body() instanceof FormBody) {
            StringBuilder formBody = new StringBuilder();
            FormBody body = (FormBody) request.body();
            if (body != null && body.size() != 0) {
                for (int i = 0; i < body.size(); i++) {
                    formBody.append(body.encodedName(i) + "=" + body.encodedValue(i) + "&");
                }
                formBody.delete(formBody.length() - 1, formBody.length());
                logLines(builder.getType(), tag, new String[]{formBody.toString()}, builder.getLogger(), true);
            }
        }
        if (builder.getLevel() == Level.BASIC || builder.getLevel() == Level.BODY) {
            logLines(builder.getType(), tag, OMITTED_REQUEST, builder.getLogger(), true);
        }
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, END_LINE);
    }

    static void printFileResponse(LoggingInterceptor.Builder builder, long chainMs, boolean isSuccessful,
                                  int code, String headers, List<String> segments) {
        String tag = builder.getTag(false);
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, RESPONSE_UP_LINE);

        logLines(builder.getType(), tag, getResponse(headers, chainMs, code, isSuccessful,
                builder.getLevel(), segments), builder.getLogger(), true);
        logLines(builder.getType(), tag, OMITTED_RESPONSE, builder.getLogger(), true);
        if (builder.getLogger() == null)
            I.log(builder.getType(), tag, END_LINE);
    }

    private static String[] getRequest(Request request, Level level) {
        String message;
        String header = request.headers().toString();
        boolean loggableHeader = level == Level.HEADERS || level == Level.BASIC;
        message = METHOD_TAG + request.method() + DOUBLE_SEPARATOR +
                (isEmpty(header) ? "" : loggableHeader ? HEADERS_TAG + LINE_SEPARATOR + dotHeaders(header) : "");
        return message.split(LINE_SEPARATOR);
    }

    private static String[] getResponse(String header, long tookMs, int code, boolean isSuccessful,
                                        Level level, List<String> segments) {
        String message;
        boolean loggableHeader = level == Level.HEADERS || level == Level.BASIC;
        String segmentString = slashSegments(segments);
        message = ((!TextUtils.isEmpty(segmentString) ? segmentString + " - " : "") + "is success : "
                + isSuccessful + " - " + RECEIVED_TAG + tookMs + "ms" + DOUBLE_SEPARATOR + STATUS_CODE_TAG +
                code + DOUBLE_SEPARATOR + (isEmpty(header) ? "" : loggableHeader ? HEADERS_TAG + LINE_SEPARATOR +
                dotHeaders(header) : ""));
        return message.split(LINE_SEPARATOR);
    }

    private static String slashSegments(List<String> segments) {
        StringBuilder segmentString = new StringBuilder();
        for (String segment : segments) {
            segmentString.append("/").append(segment);
        }
        return segmentString.toString();
    }

    private static String dotHeaders(String header) {
        String[] headers = header.split(LINE_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        String tag = "─ ";
        if (headers.length > 1) {
            for (int i = 0; i < headers.length; i++) {
                if (i == 0) {
                    tag = CORNER_UP;
                } else if (i == headers.length - 1) {
                    tag = CORNER_BOTTOM;
                } else {
                    tag = CENTER_LINE;
                }
                builder.append(tag).append(headers[i]).append("\n");
            }
        } else {
            for (String item : headers) {
                builder.append(tag).append(item).append("\n");
            }
        }
        return builder.toString();
    }

    private static void logLines(int type, String tag, String[] lines, Logger logger, boolean withLineSize) {
        for (String line : lines) {
            int lineLength = line.length();
            int MAX_LONG_SIZE = withLineSize ? 110 : lineLength;
            for (int i = 0; i <= lineLength / MAX_LONG_SIZE; i++) {
                int start = i * MAX_LONG_SIZE;
                int end = (i + 1) * MAX_LONG_SIZE;
                end = end > line.length() ? line.length() : end;
                if (logger == null) {
                    I.log(type, tag, DEFAULT_LINE + line.substring(start, end));
                } else {
                    logger.log(type, tag, line.substring(start, end));
                }
            }
        }
    }

    private static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() == null)
                return "";
            copy.body().writeTo(buffer);
            return getJsonString(buffer.readUtf8());
        } catch (final IOException e) {
            return "{\"err\": \"" + e.getMessage() + "\"}";
        }
    }

    static String getJsonString(String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        return message;
    }

}
