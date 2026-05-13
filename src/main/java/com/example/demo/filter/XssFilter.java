package com.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *  案例3：XSS 防护过滤器
 */
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // 包装请求，过滤 XSS 攻击
        XssRequestWrapper xssRequest = new XssRequestWrapper(req);
        chain.doFilter(xssRequest, response);
    }

    /**
     * 请求包装器，过滤 XSS
     */
    static class XssRequestWrapper extends HttpServletRequestWrapper {

        public XssRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }

            String[] escapedValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                escapedValues[i] = cleanXss(values[i]);
            }
            return escapedValues;
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return value != null ? cleanXss(value) : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> originalMap = super.getParameterMap();
            Map<String, String[]> filteredMap = new HashMap<>();

            for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
                String[] values = entry.getValue();
                String[] cleanedValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    cleanedValues[i] = cleanXss(values[i]);
                }
                filteredMap.put(entry.getKey(), cleanedValues);
            }

            return filteredMap;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return value != null ? cleanXss(value) : null;
        }

        private String cleanXss(String value) {
            if (value == null) {
                return null;
            }

            // HTML 转义
            value = HtmlUtils.htmlEscape(value);

            // 移除危险标签
            value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
            value = value.replaceAll("(?i)<.*?onclick.*?>", "");
            value = value.replaceAll("(?i)<.*?onload.*?>", "");

            return value;
        }
    }
}