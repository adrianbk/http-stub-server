package com.github.adrianbk.stubby.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.github.adrianbk.stubby.model.StubParam;
import com.github.adrianbk.stubby.model.StubRequest;
import com.github.adrianbk.stubby.model.StubResponse;
import com.github.adrianbk.stubby.utils.JsonUtils;

import com.sun.net.httpserver.HttpExchange;

/*
 * Transform between stubby & HttpExchange structures
 */
public class Transformer {

    public static List<StubParam> fromExchangeHeaders(HttpExchange exchange) {
        List<StubParam> result = new ArrayList<StubParam>();
        for (Map.Entry<String, List<String>> entry : exchange.getRequestHeaders().entrySet()) {
            for (String value : entry.getValue()) {
                result.add(new StubParam(entry.getKey(), value));
            }
        }
        return result;
    }

    public static List<StubParam> fromExchangeParams(HttpExchange exchange) {
        List<NameValuePair> params = URLEncodedUtils.parse(exchange.getRequestURI(), "UTF-8");
        List<StubParam> result = new ArrayList<StubParam>();
        for (NameValuePair pair : params) {
            result.add(new StubParam(pair.getName(), pair.getValue()));
        }
        return result;
    }

    public static StubRequest fromExchange(HttpExchange exchange) {
        StubRequest result = new StubRequest();
        result.setPath(exchange.getRequestURI().getPath());
        String method = exchange.getRequestMethod().toUpperCase(); // method should always be upper-case
        result.setMethod(method);
        if (!method.equals("GET")) { // GET requests don't (shouldn't) have a body or content type
            try {
                result.setBody(IOUtils.toString(exchange.getRequestBody()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        result.setParams(fromExchangeParams(exchange));
        result.setHeaders(fromExchangeHeaders(exchange));
        return result;
    }

    public static void populateExchange(StubResponse message, HttpExchange exchange) throws IOException {
        if (message.getHeaders() != null) {
            for (StubParam header : message.getHeaders()) {
                exchange.getResponseHeaders().add(header.getName(), header.getValue());
            }
        }
        exchange.sendResponseHeaders(message.getStatus(), 0); // arbitrary-length body
        if (message.getBody() instanceof String) {
            IOUtils.write(message.getBody().toString(), exchange.getResponseBody());
        } else {
            JsonUtils.serialize(exchange.getResponseBody(), message.getBody()); // assume deserialised JSON (ie, a Map) 
        }
        exchange.getResponseBody().close();
    }

}
