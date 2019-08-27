package com.nxlh.manager.amqp.bussiness;


import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.JsonUtils;
import com.nxlh.common.utils.RabbitMQUtils;
import com.nxlh.manager.amqp.RoutingKey;
import com.nxlh.manager.amqp.queue.ShoppingCarQueue;
import com.nxlh.manager.model.dbo.TbShoppingcar;
import com.nxlh.manager.model.dto.MqFailDTO;
import com.nxlh.manager.model.dto.ShoppingCarDTO;
import com.nxlh.manager.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nxlh.manager.amqp.RoutingKey.*;
import static com.nxlh.manager.amqp.queue.ShoppingCarQueue.*;

/**
 * 监听Shoppingcar队列，处理购物车序列化
 */
@Slf4j
@Component
public class ShoppingCarAmqpService extends BaseAmqpService {

    @Autowired
    private ShoppingCarService shoppingCarService;


    /**
     * 添加
     */
    @RabbitListener(queues = "shoppingcar.add")
    @RabbitHandler()
    public void add(ShoppingCarDTO model, Channel channel, Message message) throws IOException {

        this.log.debug("==========收到消息通知，开始添加【购物车】=======");
        this.log.debug(model.toString());

        try {
            if (this.shoppingCarService.add(model)) {
                this.ackOk(message, channel);
            } else {
                this.ackFail(message, channel);
            }
        } catch (Exception e) {
            this.retry(channel, message, model, RETRY_EX, SHOPPINGCAR_RETRY_ADD, FAIL_EX, SHOPPINGCAR_FAIL);
        }
    }

    /**
     * 修改
     *
     * @param model
     * @param channel
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "shoppingcar.update")
    @RabbitHandler()
    public void update(ShoppingCarDTO model, Channel channel, Message message) throws Exception {
        this.log.debug("==========收到消息通知，开始更新【购物车】=======");
        this.log.debug(model.toString());

        try {
            if (this.shoppingCarService.updateById(model)) {
                this.ackOk(message, channel);
            } else {
                this.ackFail(message, channel);
            }
        } catch (Exception ex) {
            this.retry(channel, message, model, RETRY_EX, SHOPPINGCAR_RETRY_UPDATE, FAIL_EX, SHOPPINGCAR_FAIL);
        }

    }

    /**
     * 删除车中的商品
     *
     * @param channel
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "shoppingcar.delete")
    @RabbitHandler()
    public void delete(Map<String, Object> params, Channel channel, Message message) throws Exception {
        this.log.debug("==========收到消息通知，开始删除【购物车】=======");
        this.log.debug(params.toString());

        try {

            var wxuserid = params.get("wxuserid").toString();

            var example = Example
                    .builder(TbShoppingcar.class).where(Sqls.custom().andIn("productid", (List) params.get("productids"))
                            .andEqualTo("wxuserid", wxuserid))
                    .build();
            this.shoppingCarService.remove(example);

            this.ackOk(message, channel);
        } catch (Exception e) {
            this.retry(channel, message, params, RETRY_EX, SHOPPINGCAR_RETRY_DELETE, FAIL_EX, SHOPPINGCAR_FAIL);
        }
    }


    /**
     * 统一处理失败的记录
     */
    @RabbitListener(queues = SHOPPINGCAR_FAIL)
    @RabbitHandler
    public void handleFail( Channel channel, Message message) throws IOException {

        try {

            var model = new MqFailDTO();
            model.setId(IDUtils.genUUID());
            model.setAddtime(DateUtils.now());
            model.setExchange(this.getOrigExchange(message.getMessageProperties(), "shoppingcar"));
            model.setQueue(this.getOrigQueue(message.getMessageProperties(), "shoppingcar"));
            model.setMessagedata(new String(message.getBody()));
            this.mqFailService.add(model);
            this.ackOk(message, channel);

        } catch (Exception ex) {
            ex.printStackTrace();
            this.ackFail(message, channel);
        }
    }


}
