package com.chuan.trans.service;

/**
 * @author jc
 * Date:2019/4/30
 * Time:14:20
 */
public interface TradeService {

    String getOrder() throws Exception;

    Boolean check() throws Exception;
}
