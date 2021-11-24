package io.avreen.common.jmx;

/**
 * Created by asgharnejad on 3/18/2019.
 * copy from this link
 * https://github.com/actimem/blog/tree/master/java/src/main/java/com/actimem/example/jmx/metadata
 */

import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import javax.management.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The class Avreen standard mx bean.
 */
public class AvreenStandardMXBean extends StandardMBean {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.jmx.AvreenStandardMXBean");

    private Object implementation;
    private String objectName;

    /**
     * Instantiates a new Avreen standard mx bean.
     *
     * @param implementation the implementation
     * @param objectName     the object name
     */
    public <T> AvreenStandardMXBean(T implementation, String objectName) {
        super(implementation, null, true);
        this.implementation = implementation;
        this.objectName = objectName;
    }

    @Override
    protected String getDescription(MBeanInfo beanInfo) {
        JmxDescriptionAnnotaion annotation = getMBeanInterface().getAnnotation(JmxDescriptionAnnotaion.class);
        if (annotation != null) {
            return annotation.value();
        }
        return beanInfo.getDescription();
    }

    @Override
    protected String getDescription(MBeanAttributeInfo info) {
        String attributeName = info.getName();
        String getterName = String.format("get%s%s", attributeName.substring(0, 1).toUpperCase(), attributeName.substring(1));
        Method m = methodByName(getMBeanInterface(), getterName, new String[]{});
        if (m != null) {
            JmxDescriptionAnnotaion d = m.getAnnotation(JmxDescriptionAnnotaion.class);
            if (d != null)
                return d.value();
        }
        return info.getDescription();
    }

    @Override
    protected String getDescription(MBeanOperationInfo op) {
        Method m = methodByOperation(getMBeanInterface(), op);
        if (m != null) {
            JmxDescriptionAnnotaion d = m.getAnnotation(JmxDescriptionAnnotaion.class);
            if (d != null)
                return d.value();
        }
        return op.getDescription();
    }

    @Override
    protected String getDescription(MBeanOperationInfo op, MBeanParameterInfo param, int paramNo) {
        Method m = methodByOperation(getMBeanInterface(), op);
        if (m != null) {
            JmxDescriptionAnnotaion pname = getParameterAnnotation(m, paramNo, JmxDescriptionAnnotaion.class);
            if (pname != null)
                return pname.value();
        }
        return getParameterName(op, param, paramNo);
    }

    @Override
    protected String getParameterName(MBeanOperationInfo op, MBeanParameterInfo param, int paramNo) {
        Method m = methodByOperation(getMBeanInterface(), op);
        if (m != null) {
            JmxOperationParamAnnotaion pname = getParameterAnnotation(m, paramNo, JmxOperationParamAnnotaion.class);
            if (pname != null)
                return pname.value();
        }
        return param.getName();
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return super.getMBeanInfo();
    }

    private static <A extends Annotation> A getParameterAnnotation(Method m, int paramNo, Class<A> annotation) {
        for (Annotation a : m.getParameterAnnotations()[paramNo]) {
            if (annotation.isInstance(a))
                return annotation.cast(a);
        }
        return null;
    }

    private static Method methodByName(Class<?> mbeanInterface, String name, String... paramTypes) {
        try {
            final ClassLoader loader = mbeanInterface.getClassLoader();
            final Class<?>[] paramClasses = new Class<?>[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++)
                paramClasses[i] = classForName(paramTypes[i], loader);
            return mbeanInterface.getMethod(name, paramClasses);
        } catch (RuntimeException e) {
            // avoid accidentally catching unexpected runtime exceptions
            throw e;
        } catch (Exception e) {
            return null;
        }
    }


    private static Method methodByOperation(Class<?> mbeanInterface, MBeanOperationInfo op) {
        final MBeanParameterInfo[] params = op.getSignature();
        final String[] paramTypes = new String[params.length];
        for (int i = 0; i < params.length; i++)
            paramTypes[i] = params[i].getType();

        return methodByName(mbeanInterface, op.getName(), paramTypes);
    }

    private static Class<?> classForName(String name, ClassLoader loader) throws ClassNotFoundException {
        Class<?> c = primitiveClasses.get(name);
        if (c == null)
            c = Class.forName(name, false, loader);
        return c;
    }

    private static final Map<String, Class<?>> primitiveClasses = new HashMap<String, Class<?>>();

    static {
        Class<?>[] primitives = {byte.class, short.class, int.class, long.class, float.class, double.class, char.class, boolean.class};
        for (Class<?> clazz : primitives)
            primitiveClasses.put(clazz.getName(), clazz);
    }

    public Object invoke(String actionName, Object params[], String signature[])
            throws MBeanException, ReflectionException {
        Object rval = super.invoke(actionName, params, signature);
        if (LOGGER.isInfoEnabled()) {
            StringBuilder stringBuilder = new StringBuilder();
            if (params != null) {
                for (Object o : params) {
                    stringBuilder.append(o);
                    stringBuilder.append(",");
                }
            }

            LOGGER.info("invoke objectName={} actionName={} params={}", objectName, actionName, params, stringBuilder);
        }
        return rval;
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        super.setAttribute(attribute);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("set attribute cal  objectName={} attribute={}", objectName, attribute);
        }
    }
}