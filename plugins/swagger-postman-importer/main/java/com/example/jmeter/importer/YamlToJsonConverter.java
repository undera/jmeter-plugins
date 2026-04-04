package com.example.jmeter.importer;

/**
 * Converts the subset of YAML used by standard OpenAPI specs to JSON without
 * any external dependencies (uses only the bundled org.json via relocation).
 *
 * <p>Handles: block/flow mappings, sequences, scalars, quoted strings,
 * inline JSON-style objects, multi-line values.</p>
 */
public class YamlToJsonConverter {

    private String[] lines;
    private int      lineIdx;

    public String convert(String yaml) {
        lines   = yaml.replace("\r\n", "\n").replace("\r", "\n").split("\n", -1);
        lineIdx = 0;
        StringBuilder sb = new StringBuilder();
        Object result = parseValue(-1);
        appendJson(result, sb);
        return sb.toString();
    }

    // -----------------------------------------------------------------------
    // Recursive descent
    // -----------------------------------------------------------------------

    private Object parseValue(int parentIndent) {
        skipEmptyLines();
        if (lineIdx >= lines.length) return null;
        String line    = lines[lineIdx];
        int    indent  = indentOf(line);
        String trimmed = line.trim();
        if (trimmed.startsWith("{")) return parseInlineObject(trimmed);
        if (trimmed.startsWith("[")) return parseInlineArray(trimmed);
        if (trimmed.startsWith("- ") || trimmed.equals("-")) return parseSequence(indent);
        if (trimmed.contains(":") && !trimmed.startsWith(":"))   return parseMapping(indent);
        lineIdx++;
        return unquote(trimmed);
    }

    @SuppressWarnings("unchecked")
    private java.util.Map<String, Object> parseMapping(int myIndent) {
        java.util.LinkedHashMap<String, Object> map = new java.util.LinkedHashMap<>();
        while (lineIdx < lines.length) {
            skipEmptyLines();
            if (lineIdx >= lines.length) break;
            String line   = lines[lineIdx];
            int    indent = indentOf(line);
            if (indent < myIndent) break;
            String trimmed = line.trim();
            if (!trimmed.contains(":") || trimmed.startsWith("-")) break;
            int colonIdx = firstColon(trimmed);
            if (colonIdx < 0) break;
            String key  = unquote(trimmed.substring(0, colonIdx).trim());
            String rest = trimmed.substring(colonIdx + 1).trim();
            lineIdx++;
            Object value;
            if (rest.isEmpty() || rest.equals("|") || rest.equals(">")) {
                skipEmptyLines();
                if (lineIdx < lines.length) {
                    int childIndent = indentOf(lines[lineIdx]);
                    if (childIndent > myIndent) {
                        String firstChild = lines[lineIdx].trim();
                        if (firstChild.startsWith("- ") || firstChild.equals("-"))
                            value = parseSequence(childIndent);
                        else if (firstChild.contains(":") && !firstChild.startsWith(":"))
                            value = parseMapping(childIndent);
                        else
                            value = parseScalar(childIndent);
                    } else value = null;
                } else value = null;
            } else if (rest.startsWith("{")) {
                value = parseInlineObject(rest);
            } else if (rest.startsWith("[")) {
                value = parseInlineArray(rest);
            } else {
                value = parseScalarValue(rest);
            }
            map.put(key, value);
        }
        return map;
    }

    private java.util.List<Object> parseSequence(int myIndent) {
        java.util.List<Object> list = new java.util.ArrayList<>();
        while (lineIdx < lines.length) {
            skipEmptyLines();
            if (lineIdx >= lines.length) break;
            String line   = lines[lineIdx];
            int    indent = indentOf(line);
            if (indent < myIndent) break;
            String trimmed = line.trim();
            if (!trimmed.startsWith("- ") && !trimmed.equals("-")) break;
            String afterDash = trimmed.length() > 2 ? trimmed.substring(2).trim() : "";
            lineIdx++;
            if (afterDash.isEmpty()) {
                skipEmptyLines();
                if (lineIdx < lines.length && indentOf(lines[lineIdx]) > myIndent) {
                    String child = lines[lineIdx].trim();
                    if (child.startsWith("- ")) list.add(parseSequence(indentOf(lines[lineIdx])));
                    else                         list.add(parseMapping(indentOf(lines[lineIdx])));
                }
            } else if (afterDash.startsWith("{")) {
                list.add(parseInlineObject(afterDash));
            } else if (afterDash.startsWith("[")) {
                list.add(parseInlineArray(afterDash));
            } else if (afterDash.contains(":") && !afterDash.startsWith(":")) {
                list.add(parseSingleMapping(afterDash, indent + 2));
            } else {
                list.add(parseScalarValue(afterDash));
            }
        }
        return list;
    }

    private Object parseSingleMapping(String text, int depth) {
        java.util.LinkedHashMap<String, Object> map = new java.util.LinkedHashMap<>();
        int colonIdx = firstColon(text);
        if (colonIdx < 0) return map;
        map.put(unquote(text.substring(0, colonIdx).trim()),
                parseScalarValue(text.substring(colonIdx + 1).trim()));
        while (lineIdx < lines.length) {
            skipEmptyLines();
            if (lineIdx >= lines.length || indentOf(lines[lineIdx]) < depth) break;
            String next = lines[lineIdx].trim();
            if (!next.contains(":") || next.startsWith("-")) break;
            int ci = firstColon(next);
            if (ci < 0) break;
            map.put(unquote(next.substring(0, ci).trim()),
                    parseScalarValue(next.substring(ci + 1).trim()));
            lineIdx++;
        }
        return map;
    }

    private Object parseScalar(int indent) {
        StringBuilder sb = new StringBuilder();
        while (lineIdx < lines.length) {
            if (indentOf(lines[lineIdx]) < indent) break;
            sb.append(lines[lineIdx].trim()).append(" ");
            lineIdx++;
        }
        return parseScalarValue(sb.toString().trim());
    }

    private Object parseInlineObject(String text) {
        try { return new org.json.JSONObject(text).toMap(); }
        catch (Exception e) { return text; }
    }

    private Object parseInlineArray(String text) {
        try {
            org.json.JSONArray a = new org.json.JSONArray(text);
            java.util.List<Object> list = new java.util.ArrayList<>();
            for (int i = 0; i < a.length(); i++) list.add(a.get(i));
            return list;
        } catch (Exception e) { return text; }
    }

    private Object parseScalarValue(String s) {
        if (s == null || s.isEmpty() || s.equals("null") || s.equals("~")) return null;
        if (s.equals("true")  || s.equals("yes") || s.equals("on"))  return Boolean.TRUE;
        if (s.equals("false") || s.equals("no")  || s.equals("off")) return Boolean.FALSE;
        try { return Long.parseLong(s); }   catch (NumberFormatException ignored) {}
        try { return Double.parseDouble(s); } catch (NumberFormatException ignored) {}
        return unquote(s);
    }

    @SuppressWarnings("unchecked")
    private void appendJson(Object obj, StringBuilder sb) {
        if (obj == null) {
            sb.append("null");
        } else if (obj instanceof java.util.Map) {
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) obj;
            sb.append('{');
            boolean first = true;
            for (java.util.Map.Entry<String, Object> e : map.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(jsonEscape(e.getKey())).append('"').append(':');
                appendJson(e.getValue(), sb);
            }
            sb.append('}');
        } else if (obj instanceof java.util.List) {
            java.util.List<Object> list = (java.util.List<Object>) obj;
            sb.append('[');
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(',');
                appendJson(list.get(i), sb);
            }
            sb.append(']');
        } else if (obj instanceof Boolean || obj instanceof Number) {
            sb.append(obj);
        } else {
            sb.append('"').append(jsonEscape(obj.toString())).append('"');
        }
    }

    // -----------------------------------------------------------------------
    // Utilities
    // -----------------------------------------------------------------------

    private void skipEmptyLines() {
        while (lineIdx < lines.length && lines[lineIdx].trim().isEmpty()) lineIdx++;
    }

    private int indentOf(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') i++;
        return i;
    }

    private int firstColon(String s) {
        boolean inSingle = false, inDouble = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if      (c == '\'' && !inDouble) inSingle = !inSingle;
            else if (c == '"'  && !inSingle) inDouble = !inDouble;
            else if (c == ':' && !inSingle && !inDouble) {
                if (i + 1 == s.length() || s.charAt(i + 1) == ' ') return i;
            }
        }
        return -1;
    }

    private String unquote(String s) {
        if (s == null) return "";
        s = s.trim();
        if ((s.startsWith("\"") && s.endsWith("\"")) ||
            (s.startsWith("'")  && s.endsWith("'"))) {
            return s.substring(1, s.length() - 1)
                    .replace("\\'", "'").replace("\\\"", "\"");
        }
        return s;
    }

    private String jsonEscape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
}
