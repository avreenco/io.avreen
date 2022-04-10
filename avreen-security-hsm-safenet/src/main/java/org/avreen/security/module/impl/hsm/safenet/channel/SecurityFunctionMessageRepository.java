package org.avreen.security.module.impl.hsm.safenet.channel;

import org.avreen.security.module.api.RequestBase;
import org.avreen.security.module.api.ResponseBase;
import org.avreen.security.module.api.SafenetFunctionCodeAnnotaion;
import org.avreen.security.module.api.SecurityFunctionMessage;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Security function message repository.
 */
public class SecurityFunctionMessageRepository {

    private static final String REQUEST_PKG = "org.avreen.security.module.api.request";
    private static final String RESPONSE_PKG = "org.avreen.security.module.api.response";
    private static ConcurrentHashMap<String, Class<?>> requestClasses = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Class<?>> responseClasses = new ConcurrentHashMap<>();

    /**
     * Build request message t.
     *
     * @param <T> the type parameter
     * @param FC  the fc
     * @return the t
     */
    public static <T extends RequestBase> T buildRequestMessage(String FC) {
        return (T) buildMessage(FC, requestClasses, REQUEST_PKG);
    }

    /**
     * Build response msg t.
     *
     * @param <T> the type parameter
     * @param FC  the fc
     * @return the t
     */
    public static <T extends ResponseBase> T buildResponseMsg(String FC) {
        return (T) buildMessage(FC, responseClasses, RESPONSE_PKG);
    }

    private static SecurityFunctionMessage buildMessage(String FC, ConcurrentHashMap<String, Class<?>> cacheMap, String pkName) {
        Class<?> clazz;
        if (cacheMap.containsKey(FC)) {
            clazz = cacheMap.get(FC);
        } else {
            synchronized (cacheMap) {
                if (cacheMap.containsKey(FC)) {
                    clazz = cacheMap.get(FC);
                } else {
                    clazz = getMessageClass(FC, pkName);
                    cacheMap.putIfAbsent(FC, clazz);
                }
            }
        }
        try {
            return (SecurityFunctionMessage) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    public static Set<Class<?>> getPackageClassSet2(String pkName) {
//        try {
//            final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
//            provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
//            final Set<BeanDefinition> classes = provider.findCandidateComponents(pkName);
//            HashSet<Class<?>> hashSet = new HashSet<>();
//            for (BeanDefinition bean : classes) {
//                Class<?> clazz = Class.forName(bean.getBeanClassName());
//                hashSet.add(clazz);
//            }
//            return hashSet;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static SecurityFunctionMessage buildMessage(String FC, String pkName) {
        try {
            return (SecurityFunctionMessage) getMessageClass(FC, pkName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getMessageClass(String FC, String pkName) {
        try {
            Set<Class<?>> classSet = getPackageClassSet(pkName);
            for (Class<?> clazz : classSet) {
                SafenetFunctionCodeAnnotaion safenetFunctionCode = clazz.getAnnotation(SafenetFunctionCodeAnnotaion.class);
                if (safenetFunctionCode != null) {
                    if (safenetFunctionCode.code().equals(FC)) {
                        return clazz;
                    }
                }
            }
            throw new RuntimeException("can not found entity class for " + FC);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private static Set<Class<?>> getPackageClassSet(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            HashSet<Class<?>> classes = new HashSet<>();
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
            return classes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
