package io.avreen.mq.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class Publishers queue registry.
 */
public class PublishersQueueRegistry {

    private static ConcurrentHashMap<String, List<IMsgPublisher>> publisherHashMap = new ConcurrentHashMap<>();

    /**
     * Add queue publisher.
     *
     * @param moduleName   the module name
     * @param msgPublisher the msg publisher
     */
    public static void addQueuePublisher(String moduleName, IMsgPublisher msgPublisher) {
        List<IMsgPublisher> iMsgPublishers = publisherHashMap.get(moduleName);
        if (iMsgPublishers == null)
            iMsgPublishers = new ArrayList<>();
        iMsgPublishers.add(msgPublisher);
        publisherHashMap.put(moduleName, iMsgPublishers);
    }


    /**
     * Load first publisher msg publisher.
     *
     * @param moduleName the module name
     * @return the msg publisher
     */
//    public static IMsgPublisher loadPublisher(String i_moduleName) {
//        String  pubName = null;
//        String  applyModule = i_moduleName;
//        String[]  moduleWithPublisher =  i_moduleName.split("#");
//        if(moduleWithPublisher.length>1)
//        {
//            pubName = moduleWithPublisher[1].trim();
//            applyModule = moduleWithPublisher[0].trim();
//        }
//        if(pubName==null)
//        {
//            if (publisherHashMap.containsKey(applyModule)) {
//                List<IMsgPublisher> msgPublishers = publisherHashMap.get(applyModule);
//                if (msgPublishers != null && msgPublishers.size() > 0)
//                    return msgPublishers.get(0);
//            }
//        }
//        else
//        {
//            if (publisherHashMap.containsKey(applyModule)) {
//                List<IMsgPublisher> msgPublishers = publisherHashMap.get(applyModule);
//                IMsgPublisher publisher = MessageBrokerRegistry.getPublisher(pubName);
//                if(!msgPublishers.contains(publisher))
//                    throw new RuntimeException("publisher with name="+pubName+" not registered for module="+applyModule);
//                return publisher;
//            }
//        }
//        return defaultMsgPublisher;
//    }
    public static IMsgPublisher loadFirstPublisher(String moduleName) {
        if (publisherHashMap.containsKey(moduleName)) {
            List<IMsgPublisher> msgPublishers = publisherHashMap.get(moduleName);
            if (msgPublishers != null && msgPublishers.size() > 0)
                return msgPublishers.get(0);
            return null;
        }
        return null;
    }

    /**
     * Load first publisher msg publisher.
     *
     * @param moduleName      the module name
     * @param throwIfNotExist the throw if not exist
     * @return the msg publisher
     */
    public static IMsgPublisher loadFirstPublisher(String moduleName, boolean throwIfNotExist) {

        IMsgPublisher msgPublisher = loadFirstPublisher(moduleName);
        if (msgPublisher == null && throwIfNotExist)
            throw new RuntimeException("can not found first publisher for module name=" + moduleName);
        return msgPublisher;
    }


}
