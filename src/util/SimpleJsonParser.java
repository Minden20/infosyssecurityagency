package util;

import java.util.ArrayList;
import java.util.List;

public class SimpleJsonParser {
    /**
     * Представляє JSON об'єкт як пари ключ-значення.
     */
    public static class JsonObject {
        private java.util.Map<String, Object> map = new java.util.HashMap<>();

        /**
         * Додає пару ключ-значення до об'єкта.
         * 
         * @param key ключ
         * @param value значення
         */
        public void put(String key, Object value) {
            map.put(key, value);
        }

        /**
         * Отримує значення за ключем.
         * 
         * @param key ключ
         * @return значення або null
         */
        public Object get(String key) {
            return map.get(key);
        }

        /**
         * Перевіряє, чи містить об'єкт вказаний ключ.
         * 
         * @param key ключ
         * @return true, якщо ключ існує, false - інакше
         */
        public boolean containsKey(String key) {
            return map.containsKey(key);
        }
    }

    /**
     * Парсить JSON рядок у список об'єктів.
     * 
     * @param json JSON рядок
     * @return список JSON об'єктів
     */
    public static List<JsonObject> parseArray(String json) {
        List<JsonObject> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) {
            return result;
        }
        
        json = json.trim();
        
        if (!json.startsWith("[") || !json.endsWith("]")) {
            return result;
        }
        
        // Видаляємо квадратні дужки
        json = json.substring(1, json.length() - 1).trim();
        
        if (json.isEmpty()) {
            return result;
        }
        
        // Парсимо об'єкти з урахуванням вкладених структур
        int braceCount = 0;
        int start = -1;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (braceCount == 0) {
                    start = i;
                }
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0 && start != -1) {
                    String objectStr = json.substring(start, i + 1);
                    JsonObject obj = parseObject(objectStr);
                    if (obj != null) {
                        result.add(obj);
                    }
                    start = -1;
                }
            }
        }
        
        return result;
    }

    /**
     * Парсить JSON рядок у об'єкт.
     * 
     * @param json JSON рядок
     * @return JSON об'єкт або null
     */
    public static JsonObject parseObject(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        
        JsonObject obj = new JsonObject();
        json = json.trim();
        
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return null;
        }
        
        // Видаляємо фігурні дужки
        json = json.substring(1, json.length() - 1).trim();
        
        if (json.isEmpty()) {
            return obj;
        }
        
        // Парсимо пари ключ:значення з урахуванням вкладених структур
        List<String> pairs = new ArrayList<>();
        int braceCount = 0;
        int start = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
            } else if (c == ',' && braceCount == 0) {
                pairs.add(json.substring(start, i).trim());
                start = i + 1;
            }
        }
        if (start < json.length()) {
            pairs.add(json.substring(start).trim());
        }
        
        for (String pair : pairs) {
            if (pair.isEmpty()) continue;
            
            int colonIndex = pair.indexOf(':');
            if (colonIndex == -1) continue;
            
            String key = pair.substring(0, colonIndex).trim().replaceAll("^\"|\"$", "");
            String value = pair.substring(colonIndex + 1).trim();
            
            // Обробка значень
            if (value.startsWith("\"") && value.endsWith("\"")) {
                // Рядок
                obj.put(key, value.substring(1, value.length() - 1));
            } else if (value.matches("\\d+")) {
                // Число
                obj.put(key, Long.parseLong(value));
            } else if (value.equals("true") || value.equals("false")) {
                // Булеве значення
                obj.put(key, Boolean.parseBoolean(value));
            } else {
                obj.put(key, value);
            }
        }
        
        return obj;
    }

    /**
     * Конвертує об'єкт у JSON рядок.
     * 
     * @param obj об'єкт для конвертації
     * @return JSON рядок
     */
    public static String toJsonString(JsonObject obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (java.util.Map.Entry<String, Object> entry : obj.map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else {
                sb.append(value);
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Конвертує список об'єктів у JSON рядок.
     * 
     * @param objects список об'єктів
     * @return JSON рядок
     */
    public static String arrayToJsonString(List<JsonObject> objects) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < objects.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(toJsonString(objects.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
}

