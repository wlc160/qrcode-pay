package com.qrpay.demo.redis.service.impl;

import com.qrpay.demo.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 工程名:qrcode-pay
 * 包名:com.qrpay.demo.redis.service.impl
 * 文件名:RedisServiceImpl
 * description:
 *
 * @author lcwen
 * @version V1.0: RedisServiceImpl.java 2020/3/18 15:04
 **/
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redis;

    private static final String ORDER = "ORDER";

    private static final String STATUS = "STATUS_";

    private static final  String QR_CODE = "QR_CODE";

    @Override
    public void setNewOrder(String orderNum) throws Exception {
        redis.opsForValue().set(ORDER, orderNum, 10l, TimeUnit.MINUTES);
        setOrderStatus(orderNum,1);//未支付
    }

    @Override
    public String getCurrOrder() throws Exception {
        Object obj = redis.opsForValue().get(ORDER);
        return obj == null ? null:obj.toString();
    }

    @Override
    public void setOrderStatus(String orderNum, int status) throws Exception {
        redis.opsForValue().set(STATUS + orderNum, status, 10l, TimeUnit.MINUTES);
    }

    @Override
    public String getOrderStatus(String orderNum) throws Exception {
        Object obj = redis.opsForValue().get(STATUS + orderNum);
        return obj == null ? null:obj.toString();
    }

    @Override
    public void setQrCode(String url) throws Exception {
        redis.opsForValue().set(QR_CODE, url,1l,TimeUnit.HOURS);
    }

    @Override
    public String getQrCode() throws Exception {
        Object obj = redis.opsForValue().get(QR_CODE);
        return obj == null ? null:obj.toString();
    }

    @Override
    public void removeOrder(String orderNum) throws Exception {
        Object obj = redis.opsForValue().get(ORDER);
        if(obj != null && orderNum.equals(obj.toString())){
            redis.delete(ORDER);
        }
    }
}
