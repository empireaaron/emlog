/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.empire.emlog.agent.flume.log4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Tracer's tool class, this tool class is an internal tool class, please do not rely on non-Tracer related JAR
 * packages.
 */
public class TracerUtils {

    private static int TRACE_PENETRATE_ATTRIBUTE_MAX_LENGTH = -1;

    private static int TRACER_SYSTEM_PENETRATE_ATTRIBUTE_MAX_LENGTH = -1;

    private static final String KEY_OF_CURRENT_ZONE = "com.alipay.ldc.zone";

    public static final String CURRENT_ZONE = System.getProperty(KEY_OF_CURRENT_ZONE);

    private static String P_ID_CACHE = null;

    /**
     * This method can be a better way under JDK9, but in the current JDK version, it can only be implemented in this
     * way.
     *
     * In Mac OS , JDK6，JDK7，JDK8 ,it's OK In Linux OS,JDK6，JDK7，JDK8 ,it's OK
     *
     * @return Process ID
     */
    static String getPID() {
        // check pid is cached
        if (P_ID_CACHE != null) {
            return P_ID_CACHE;
        }
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();

        if (StringUtils.isBlank(processName)) {
            return StringUtils.EMPTY_STRING;
        }

        String[] processSplitName = processName.split("@");

        if (processSplitName.length == 0) {
            return StringUtils.EMPTY_STRING;
        }

        String pid = processSplitName[0];

        if (StringUtils.isBlank(pid)) {
            return StringUtils.EMPTY_STRING;
        }
        P_ID_CACHE = pid;
        return pid;
    }

    static String getInetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

}
