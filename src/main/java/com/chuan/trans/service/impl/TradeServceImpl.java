package com.chuan.trans.service.impl;

import com.chuan.trans.annotation.TransMessage;
import com.chuan.trans.service.TradeService;
import org.springframework.stereotype.Service;

/**
 * @author jc
 * Date:2019/4/30
 * Time:14:21
 */
@Service
public class TradeServceImpl implements TradeService {



    @Override
    @TransMessage(topic = "", tags = "")
    public String getOrder() throws Exception {
        return null;
    }

    @Override
    public Boolean check() throws Exception {
        return null;
    }
}
