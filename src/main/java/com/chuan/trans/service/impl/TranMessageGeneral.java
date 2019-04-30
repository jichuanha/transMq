package com.chuan.trans.service.impl;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.chuan.trans.message.LocalTransactionCheckerImpl;
import com.chuan.trans.service.ProducerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author jc
 * Date:2019/4/29
 * Time:15:54
 */
public class TranMessageGeneral implements ProducerClient {

    private final static Logger log = LoggerFactory.getLogger(TranMessageGeneral.class);

    private TransactionProducer producer;
    private String groupId;
    private String add;

    public void init() {
        Properties properties = new Properties();
        // 您在控制台创建的 Group ID 注意：事务消息的 Group ID 不能与其他类型消息的 Group ID 共用
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
        // 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, "LTAI3ZGESKTFsdVW");
        // 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "x7JykSfC7uAajQHcEp8t1Np6NbHyjA");
        // 设置 TCP 接入域名，进入控制台的实例管理页面的“获取接入点信息”区域查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR, add);
        // 配置检查本地事务的方法
        producer = ONSFactory.createTransactionProducer(properties,
                new LocalTransactionCheckerImpl());
        producer.start();

    }


    public TranMessageGeneral() {}

    public TranMessageGeneral(String groupId, String add) {
        this.groupId = groupId;
        this.add = add;
    }

    @Override
    public void sendMessage() throws Exception {

    }

    public TransactionProducer getProducer() {
        return this.producer;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setAdd(String add) {
        this.add = add;
    }

}
