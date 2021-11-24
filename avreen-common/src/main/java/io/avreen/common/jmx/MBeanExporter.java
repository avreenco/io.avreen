package io.avreen.common.jmx;


import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

/**
 * The class M bean exporter.
 */
public class MBeanExporter {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.jmx.MBeanExporter");

    /**
     * Gets m bean server.
     *
     * @return the m bean server
     */
    public static MBeanServer getMBeanServer() {
        MBeanServer server;
        ArrayList mbeanServerList =
                MBeanServerFactory.findMBeanServer(null);
        if (mbeanServerList.isEmpty()) {
            server = ManagementFactory.getPlatformMBeanServer();
        } else {
            server = (MBeanServer) mbeanServerList.get(0);
        }
        return server;

    }

    /**
     * Register.
     *
     * @param object     the object
     * @param objectName the object name
     */
    public static synchronized void register(Object object,
                                             String objectName) {
        try {
            MBeanServer mBeanServer = getMBeanServer();
            ObjectName objectNameIns = ObjectName.getInstance(objectName);
            if (!mBeanServer.isRegistered(objectNameIns)) {
                //  mBeanServer.registerMBean(object, objectNameIns);
                mBeanServer.registerMBean(new AvreenStandardMXBean(object, objectName), objectNameIns);
            } else {
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn("object already registered name={}", objectName);
            }

        } catch (Throwable e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("error in register object name" + objectName, e);
        }
    }

    /**
     * Unregister.
     *
     * @param objectName the object name
     */
    public static synchronized void unregister(String objectName) {
        try {

            MBeanServer mBeanServer = getMBeanServer();
            ObjectName objectNameIns = ObjectName.getInstance(objectName);
            if (mBeanServer.isRegistered(objectNameIns))
                mBeanServer.unregisterMBean(objectNameIns);

        } catch (Throwable e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("error in unregister object name" + objectName, e);
        }
    }

}
