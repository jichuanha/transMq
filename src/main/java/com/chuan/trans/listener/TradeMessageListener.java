package com.chuan.trans.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;


/**
 * 消息监听器
 * @author lizg
 * @date 2019/03/18
 */
public class TradeMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(TradeMessageListener.class);


    @Override
    public Action consume(Message msg, ConsumeContext context) {
        log.info("receive a message, msgId: {}, key: {},tag:{}", msg.getMsgID(), msg.getKey(), msg.getTag());

        if ("leafActOrder".equals(msg.getTag())) {
            try {
                String msgStr = new String(msg.getBody(), Charset.forName("UTF-8"));
                //JSONObject jsonObject = JSONObject.parseObject(msgStr);
                //log.info("msg++  :{}", JsonUtil.toJson(jsonObject));

                return Action.CommitMessage;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("no tags are matched, msgId: {}, topic: {}, tags: {}", msg.getMsgID(), msg.getTopic(), msg.getTag());
        }
        return Action.CommitMessage;
    }
}
