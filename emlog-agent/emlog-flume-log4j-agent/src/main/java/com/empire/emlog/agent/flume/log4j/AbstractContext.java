//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.empire.emlog.agent.flume.log4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aaron.xu
 */
public abstract class AbstractContext {
    private static ThreadLocal<Map<String, Object>> tl = new ThreadLocal<>();
    private static final int MAP_CAP = 16;
    private static final float MAP_LOAD_FACTOR = 1.0F;
    private static final String SB = "sb";
    private static final int SB_CAP = 256;

    public AbstractContext() {}

    static StringBuilder getStringBuilder() {
        return getStringBuilder(true);
    }

    private static StringBuilder getStringBuilder(boolean clean) {
        Map<String, Object> m = getMap();
        StringBuilder b = (StringBuilder)m.get(SB);
        if (b == null) {
            b = new StringBuilder(SB_CAP);
            m.put(SB, b);
        } else if (clean) {
            b.delete(0, b.length());
        }

        return b;
    }

    private static Map<String, Object> getMap() {
        Map<String, Object> m = tl.get();
        if (m == null) {
            m = new HashMap<>(MAP_CAP, MAP_LOAD_FACTOR);
            tl.set(m);
        }

        return m;
    }

}
