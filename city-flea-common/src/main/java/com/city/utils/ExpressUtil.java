package com.city.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.city.properties.ExpressProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class ExpressUtil {

    @Resource
    private ExpressProperties expressProperties;

    private static ExpressUtil expressUtil;

    @PostConstruct
    public void init() {
        expressUtil = this;
    }

    public static String getApiUrl() {
        return expressUtil.expressProperties.getApiUrl();
    }

    public static String getEbusinessId() {
        return expressUtil.expressProperties.getEbusinessId();
    }

    public static String getApiKey() {
        return expressUtil.expressProperties.getApiKey();
    }

    public static String getPushUrl() {
        return expressUtil.expressProperties.getPushUrl();
    }

    public static boolean isConfigured() {
        return StringUtils.hasText(getEbusinessId()) && StringUtils.hasText(getApiKey());
    }

    public static String orderTracesSub(String requestData) {
        if (!isConfigured()) {
            log.warn("快递鸟API未配置，跳过运单订阅");
            return "{\"Success\":true,\"Reason\":\"Mock Mode\"}";
        }
        String reqURL = getApiUrl() + "/api/OOrderService";
        return submit(reqURL, requestData);
    }

    public static String getOrderTracesByJson(String requestData) {
        if (!isConfigured()) {
            log.warn("快递鸟API未配置，返回模拟轨迹数据");
            return buildMockTracesResponse(requestData);
        }
        String reqURL = getApiUrl() + "/api/Eorderservice/getOrdersTraces";
        return submit(reqURL, requestData);
    }

    public static String logisticsTrack(String requestData) {
        if (!isConfigured()) {
            log.warn("快递鸟API未配置，返回模拟轨迹数据");
            return buildMockTracesResponse(requestData);
        }
        String reqURL = getApiUrl() + "/api/Eorderservice/LogisticsTrack";
        return submit(reqURL, requestData);
    }

    private static String submit(String reqURL, String requestData) {
        try {
            String sign = encode(requestData);
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Data-Type", "json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            String params = "RequestData=" + urlEncode(requestData) + "&EBusinessID=" + getEbusinessId() + "&RequestType=1002&DataSign=" + urlEncode(sign) + "&Format=JSON";
            conn.getOutputStream().write(params.getBytes(StandardCharsets.UTF_8));
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("快递鸟API调用失败", e);
            return null;
        }
    }

    private static String encode(String data) {
        try {
            String sign = data + getApiKey();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(sign.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    private static String urlEncode(String str) {
        try {
            return java.net.URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return str;
        }
    }

    public static String buildTracesSubRequest(Long orderId, String expressCode, String trackingNumber) {
        Map<String, Object> request = new HashMap<>();
        request.put("OrderCode", "");
        request.put("ShipperCode", expressCode);
        request.put("LogisticCode", trackingNumber);
        request.put("IsNotice", 1);

        Map<String, Object> callback = new HashMap<>();
        callback.put("Environment", 0);
        callback.put("Callback", StringUtils.hasText(getPushUrl()) ? getPushUrl() : "");
        request.put("Callback", callback);

        return JSON.toJSONString(request);
    }

    public static String buildTrackRequest(String expressCode, String trackingNumber) {
        Map<String, Object> request = new HashMap<>();
        request.put("OrderCode", "");
        request.put("ShipperCode", expressCode);
        request.put("LogisticCode", trackingNumber);
        request.put("IsNotice", 0);

        return JSON.toJSONString(request);
    }

    private static String buildMockTracesResponse(String requestData) {
        try {
            JSONObject request = JSON.parseObject(requestData);
            String expressCode = request.getString("ShipperCode");
            String trackingNumber = request.getString("LogisticCode");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            JSONArray traces = new JSONArray();

            JSONObject trace1 = new JSONObject();
            trace1.put("AcceptTime", now.minusDays(2).format(formatter));
            trace1.put("AcceptStation", "【" + getExpressName(expressCode) + "】包裹已揽收，正在运输中");
            traces.add(trace1);

            JSONObject trace2 = new JSONObject();
            trace2.put("AcceptTime", now.minusDays(1).format(formatter));
            trace2.put("AcceptStation", "【" + getExpressName(expressCode) + "】快件已到达【" + getCity(expressCode, 0) + "转运中心】");
            traces.add(trace2);

            JSONObject trace3 = new JSONObject();
            trace3.put("AcceptTime", now.minusHours(12).format(formatter));
            trace3.put("AcceptStation", "【" + getExpressName(expressCode) + "】快件已从【" + getCity(expressCode, 0) + "转运中心】发出，正在发往【" + getCity(expressCode, 1) + "转运中心】");
            traces.add(trace3);

            JSONObject trace4 = new JSONObject();
            trace4.put("AcceptTime", now.minusHours(6).format(formatter));
            trace4.put("AcceptStation", "【" + getExpressName(expressCode) + "】快件已到达【" + getCity(expressCode, 1) + "转运中心】");
            traces.add(trace4);

            JSONObject trace5 = new JSONObject();
            trace5.put("AcceptTime", now.minusHours(2).format(formatter));
            trace5.put("AcceptStation", "【" + getExpressName(expressCode) + "】快件正在派送中，预计今日送达【" + getCity(expressCode, 2) + "】");
            traces.add(trace5);

            JSONObject result = new JSONObject();
            result.put("Success", true);
            result.put("ShipperCode", expressCode);
            result.put("LogisticCode", trackingNumber);
            result.put("Traces", traces);
            result.put("State", "3");
            result.put("StateCom", "运输中");

            return result.toJSONString();
        } catch (Exception e) {
            log.error("生成模拟轨迹数据失败", e);
            JSONObject result = new JSONObject();
            result.put("Success", false);
            result.put("Reason", "轨迹查询失败");
            return result.toJSONString();
        }
    }

    private static String getExpressName(String code) {
        if (code == null) return "快递";
        switch (code.toUpperCase()) {
            case "SF": return "顺丰速运";
            case "YTO": return "圆通速递";
            case "ZTO": return "中通快递";
            case "STO": return "申通快递";
            case "YD": return "韵达快递";
            case "EMS": return "邮政EMS";
            case "YZPY": return "邮政平邮";
            default: return "快递";
        }
    }

    private static String getCity(String code, int index) {
        String[] cities = {"广州", "深圳", "上海", "北京", "杭州", "成都"};
        return cities[Math.abs(code.hashCode() + index) % cities.length];
    }

    public static List<Map<String, Object>> parseTracesResponse(String response) {
        List<Map<String, Object>> traces = new ArrayList<>();
        if (!StringUtils.hasText(response)) {
            return traces;
        }
        try {
            JSONObject json = JSON.parseObject(response);
            if ("true".equals(json.getString("Success")) || json.getBoolean("Success")) {
                Object tracesObj = json.get("Traces");
                if (tracesObj instanceof JSONArray) {
                    JSONArray array = (JSONArray) tracesObj;
                    for (int i = 0; i < array.size(); i++) {
                        traces.add(array.getJSONObject(i));
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析快递轨迹响应失败", e);
        }
        return traces;
    }
}
