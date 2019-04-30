package com.chuan.trans.message;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.chuan.trans.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.zip.CRC32;

/**
 * 当发送的消息是unknown时,会通过这个check方法来检查本地事务的状态.在

 * @author jc
 * Date:2019/4/28
 * Time:11:24
 */
public class LocalTransactionCheckerImpl implements LocalTransactionChecker {

    private final static Logger log = LoggerFactory.getLogger(LocalTransactionCheckerImpl.class);

    @Autowired
    private TradeService tradeService;

    @Override
    public TransactionStatus check(Message msg) {
        //消息 ID（有可能消息体一样，但消息 ID 不一样，当前消息属于半消息，所以消息 ID 在控制台无法查询）
        String msgId = msg.getMsgID();
        //消息体内容进行 crc32，也可以使用其它的方法如 MD5
        long crc32Id = crc32Code(msg.getBody());
        //消息 ID、消息本 crc32Id 主要是用来防止消息重复
        //如果业务本身是幂等的，可以忽略，否则需要利用 msgId 或 crc32Id 来做幂等
        //如果要求消息绝对不重复，推荐做法是对消息体使用 crc32 或 MD5 来防止重复消息
        //业务自己的参数对象，这里只是一个示例，需要您根据实际情况来处理
        Object businessServiceArgs = new Object();
        TransactionStatus transactionStatus = TransactionStatus.Unknow;
        try {
            //TODO  本地事务检测方法
            Boolean isCommit = tradeService.check();
            if (isCommit) {
                //本地事务已成功则提交消息
                transactionStatus = TransactionStatus.CommitTransaction;
            } else {
                //本地事务已失败则回滚消息
                transactionStatus = TransactionStatus.RollbackTransaction;
            }
        } catch (Exception e) {
            log.error("Message Id:{}", msgId, e);
        }
        log.warn("Message Id:{}transactionStatus:{}", msgId, transactionStatus.name());
        return transactionStatus;
    }

    private static long crc32Code(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }
}
