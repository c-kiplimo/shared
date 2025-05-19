package com.collicode.shared.util.bag;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractSmartMap implements Map<String, Object>, Cloneable, Serializable, Getters {
    private static final long serialVersionUID = 1L;
    private static final Pattern arrayIndexPattern = Pattern.compile("(.*)\\[(.*)\\]");
    protected Map<String, Object> store = null;

    AbstractSmartMap() {
    }

    protected abstract Map<String, Object> createMap();

    protected abstract Map<String, Object> createMap(Map<String, Object> map);

    public Object put(String keyPath, Object value) {
        String[] keys = keyPath.split("\\.");
        Map<String, Object> map = null;

        for (int i = 0; i <= keys.length - 2; ++i) {
            String thisKey = keys[i];
            Object tmpObject = this._get(thisKey, map, false);
            if (tmpObject == null) {
                tmpObject = this._create(thisKey, (Object) null, map, true);
                if (!(tmpObject instanceof Map)) {
                    throw new IllegalArgumentException("Property '" + thisKey + "' is not a map");
                }

                map = (Map) tmpObject;
            } else {
                if (!(tmpObject instanceof Map)) {
                    throw new IllegalArgumentException("Property '" + thisKey + "' is not a map");
                }

                map = (Map) tmpObject;
            }
        }

        return this._create(keys[keys.length - 1], value, map, false);
    }

    private Object _create(String key, Object value, Map<String, Object> map, boolean createSubObject) {
        Matcher m = arrayIndexPattern.matcher(key);
        boolean hasValue = value != null;
        if (!m.matches()) {
            Map tmpMap;
            if (map == null) {
                if (!hasValue && createSubObject) {
                    tmpMap = this.createMap();
                    this.store.put(key, tmpMap);
                    return tmpMap;
                } else {
                    this.store.put(key, value);
                    return value;
                }
            } else if (!hasValue && createSubObject) {
                tmpMap = this.createMap();
                map.put(key, tmpMap);
                return tmpMap;
            } else {
                map.put(key, value);
                return value;
            }
        } else {
            String keyName = m.group(1);
            Object tmpObject = null;
            if (map == null) {
                tmpObject = this.store.get(keyName);
                if (tmpObject == null) {
                    tmpObject = new ArrayList();
                    this.store.put(keyName, tmpObject);
                }
            } else {
                tmpObject = map.get(keyName);
                if (tmpObject == null) {
                    tmpObject = new ArrayList();
                    map.put(keyName, tmpObject);
                }
            }

            if (!(tmpObject instanceof List)) {
                throw new IllegalArgumentException("Property '" + key + "' is not an array");
            } else {
                List<Object> tmpList = (List) tmpObject;
                Integer index = tmpList.size() - 1;
                if (!"".equals(m.group(2))) {
                    index = Integer.parseInt(m.group(2));
                }

                if (hasValue) {
                    tmpList.add(index, value);
                    return value;
                } else {
                    Map<String, Object> tmpMap = this.createMap();
                    tmpList.add(index, tmpMap);
                    return tmpMap;
                }
            }
        }
    }

    private Object _get(String key, Map<String, Object> map) throws IllegalArgumentException {
        return this._get(key, map, true);
    }

    private Object _get(String key, Map<String, Object> map, boolean retrievalMode) throws IllegalArgumentException {
        Matcher m = arrayIndexPattern.matcher(key);
        if (!m.matches()) {
            return map == null ? this.store.get(key) : map.get(key);
        } else {
            String keyName = m.group(1);
            Object tmpObject = null;
            if (map == null) {
                tmpObject = this.store.get(keyName);
            } else {
                tmpObject = map.get(keyName);
            }

            if (!(tmpObject instanceof List)) {
                if (retrievalMode) {
                    throw new IllegalArgumentException("Property '" + key + "' is not an array");
                } else {
                    return null;
                }
            } else {
                List<Map<String, Object>> tmpList = (List) tmpObject;
                Integer index = tmpList.size() - 1;
                if (!"".equals(m.group(2))) {
                    index = Integer.parseInt(m.group(2));
                }

                return tmpList.size() > index ? tmpList.get(index) : null;
            }
        }
    }

    private Object _remove(String key, Map<String, Object> map) throws IllegalArgumentException {
        Matcher m = arrayIndexPattern.matcher(key);
        if (!m.matches()) {
            return map == null ? this.store.remove(key) : map.remove(key);
        } else {
            String keyName = m.group(1);
            Object tmpObject = null;
            if (map == null) {
                tmpObject = this.store.get(keyName);
            } else {
                tmpObject = map.get(keyName);
            }

            if (!(tmpObject instanceof List)) {
                throw new IllegalArgumentException("Property '" + key + "' is not an array");
            } else {
                List<Map<String, Object>> tmpList = (List) tmpObject;
                int index = tmpList.size() - 1;
                if (!"".equals(m.group(2))) {
                    index = Integer.parseInt(m.group(2));
                }

                return tmpList.remove(index);
            }
        }
    }

    public Object get(Object keyPath) {
        if (keyPath == null) {
            return null;
        } else {
            String[] keys = ((String) keyPath).split("\\.");
            if (keys.length <= 1) {
                return this._get(keys[0], (Map) null);
            } else {
                Map<String, Object> map = this.findLastMapInKeyPath((String) keyPath);
                return map == null ? null : this._get(keys[keys.length - 1], map);
            }
        }
    }

    public boolean containsKey(Object keyPath) {
        String[] keys = ((String) keyPath).split("\\.");
        if (keys.length <= 1) {
            try {
                if (this._get(keys[0], (Map) null) != null) {
                    return true;
                }
            } catch (Exception var5) {
            }

            return false;
        } else {
            Map<String, Object> map = this.findLastMapInKeyPath((String) keyPath);
            if (map == null) {
                return false;
            } else {
                try {
                    if (this._get(keys[keys.length - 1], map) != null) {
                        return true;
                    }
                } catch (Exception var6) {
                }

                return false;
            }
        }
    }

    public Object remove(Object keyPath) {
        String[] keys = ((String) keyPath).split("\\.");
        if (keys.length <= 1) {
            return this._remove(keys[0], (Map) null);
        } else {
            Map<String, Object> map = this.findLastMapInKeyPath((String) keyPath);
            return map == null ? null : this._remove(keys[keys.length - 1], map);
        }
    }

    private Map<String, Object> findLastMapInKeyPath(String keyPath) throws IllegalArgumentException {
        String[] keys = keyPath.split("\\.");
        Map<String, Object> map = null;

        for (int i = 0; i <= keys.length - 2; ++i) {
            String thisKey = keys[i];
            Object tmpObject = this._get(thisKey, map);
            if (tmpObject == null) {
                return null;
            }

            if (!(tmpObject instanceof Map)) {
                throw new IllegalArgumentException("Property '" + thisKey + "' is not a map");
            }

            map = (Map) tmpObject;
        }

        return map;
    }

    protected Map<String, Object> parseMap(Map<String, Object> map) {
        Map<String, Object> result = this.createMap();
        Iterator var3 = map.entrySet().iterator();

        while (var3.hasNext()) {
            Entry<String, Object> entry = (Entry) var3.next();
            if (entry.getValue() instanceof Map) {
                result.put(entry.getKey(), this.parseMap((Map) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                result.put(entry.getKey(), this.parseList((List) entry.getValue()));
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    private List<Object> parseList(List<Object> list) {
        List<Object> result = new ArrayList();
        Iterator var3 = list.iterator();

        while (var3.hasNext()) {
            Object o = var3.next();
            if (o instanceof Map) {
                result.add(this.parseMap((Map) o));
            } else if (o instanceof List) {
                result.add(this.parseList((List) o));
            } else {
                result.add(o);
            }
        }

        return result;
    }

    public int size() {
        return this.store.size();
    }

    public boolean isEmpty() {
        return this.store.isEmpty();
    }

    public boolean containsValue(Object value) {
        return this.containsValue(value);
    }

    public void putAll(Map<? extends String, ?> m) {
        this.store.putAll(m);
    }

    public void clear() {
        this.store.clear();
    }

    public Set<String> keySet() {
        return this.store.keySet();
    }

    public Collection<Object> values() {
        return this.store.values();
    }

    public Set<Entry<String, Object>> entrySet() {
        return this.store.entrySet();
    }
}


