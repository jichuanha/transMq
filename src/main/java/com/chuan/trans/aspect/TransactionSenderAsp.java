package com.chuan.trans.aspect;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.chuan.trans.annotation.TransMessage;
import com.chuan.trans.service.impl.TranMessageGeneral;
import com.chuan.trans.util.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.zip.CRC32;

/**
 * @author jc
 * Date:2019/4/28
 * Time:16:24
 */
@Component
@Aspect
public class TransactionSenderAsp {

    @Autowired
    private TranMessageGeneral tranMessageGeneral;

    private final static Logger log = LoggerFactory.getLogger(TransactionSenderAsp.class);

    @Pointcut("@annotation(com.chuan.trans.annotation.TransMessage)")
    public void annotationSender(){

    }

    @Around("annotationSender() && @annotation(rd)")
    public void sendMessage(final ProceedingJoinPoint joinPoint, TransMessage rd) {

        String topic = rd.topic();
        String tags = rd.tags();
        log.info("topic :{} , tags :{}",topic, tags);
        Message msg = new Message(topic, tags, "hello word !".getBytes());
        try {
            SendResult sendResult = tranMessageGeneral.getProducer().send(msg, new LocalTransactionExecuter() {
                @Override
                public TransactionStatus execute(Message msg, Object arg){
                    // 消息 ID（有可能消息体一样，但消息 ID 不一样，当前消息 ID 在控制台无法查询）
                    String msgId = msg.getMsgID();
                    // 消息体内容进行 crc32，也可以使用其它的如 MD5
                    long crc32Id = crc32Code(msg.getBody());
                    // 消息 ID 和 crc32id 主要是用来防止消息重复
                    // 如果业务本身是幂等的，可以忽略，否则需要利用 msgId 或 crc32Id 来做幂等
                    // 如果要求消息绝对不重复，推荐做法是对消息体 body 使用 crc32 或 MD5 来防止重复消息
                    TransactionStatus transactionStatus = TransactionStatus.Unknow;
                    try {
                        boolean proceed =(boolean) joinPoint.proceed();
                        log.info("proceed11 :{}", JsonUtil.toJson(proceed));
                        if(proceed) {
                            // 本地事务成功则提交消息
                            transactionStatus = TransactionStatus.CommitTransaction;
                        }else {
                            // 本地事务失败则回滚消息
                            transactionStatus = TransactionStatus.RollbackTransaction;
                        }

                    } catch (Throwable e) {
                        log.error("Message Id:{}", msgId, e);
                    }
                    System.out.println(msg.getMsgID());
                    log.warn("Message Id:{}transactionStatus:{}", msgId, transactionStatus.name());
                    return transactionStatus;
                }
            }, null);
        }
        catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            System.out.println(new Date() + " Send mq message failed. Topic is:" + msg.getTopic());
            e.printStackTrace();
        }
    }

    private static long crc32Code(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }
}
