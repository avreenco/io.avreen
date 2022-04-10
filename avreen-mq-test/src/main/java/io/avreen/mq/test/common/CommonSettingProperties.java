package io.avreen.mq.test.common;

import io.avreen.common.util.SystemPropUtil;

class CommonSettingProperties {

    private static CommonSettingProperties ins = new CommonSettingProperties();

    private CommonSettingProperties() {

    }
    public static CommonSettingProperties instance() {
        return ins;
    }
    public String queueName = SystemPropUtil.get("test.queue-name", "qt1");
    public int producer_node_count = SystemPropUtil.getInt("test.producer-node-count", 20);
    public int consumer_node_count = SystemPropUtil.getInt("test.consumer-node-count", 3);
    public int body_size = SystemPropUtil.getInt("test.body-size", 500);

}
