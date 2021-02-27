//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.empire.emlog.agent.flume.log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aaron.xu
 */
public abstract class AbstractContext {
    private static ThreadLocal<Map<String, Object>> tl = new ThreadLocal<>();
    private static final int mapCap = 16;
    private static final float mapLoadFactor = 1.0F;
    private static final String sb = "sb";
    private static final int sbCap = 256;

    public AbstractContext() {}

    static StringBuilder getStringBuilder() {
        return getStringBuilder(true);
    }

    private static StringBuilder getStringBuilder(boolean clean) {
        Map<String, Object> m = getMap();
        StringBuilder b = (StringBuilder)m.get(sb);
        if (b == null) {
            b = new StringBuilder(sbCap);
            m.put(sb, b);
        } else if (clean) {
            b.delete(0, b.length());
        }

        return b;
    }

    private static Map<String, Object> getMap() {
        Map<String, Object> m = tl.get();
        if (m == null) {
            m = new HashMap<>(mapCap, mapLoadFactor);
            tl.set(m);
        }

        return m;
    }

}
