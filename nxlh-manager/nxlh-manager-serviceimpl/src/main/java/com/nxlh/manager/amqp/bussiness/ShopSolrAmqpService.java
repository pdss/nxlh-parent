package com.nxlh.manager.amqp.bussiness;

import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.model.dto.LogDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.enums.LogLevelEnums;
import com.nxlh.manager.service.LogService;
import com.nxlh.manager.solr.repository.ShopSolrRepository;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.nxlh.manager.amqp.queue.ShopQueue.SHOP_SOLR_DELETE_QUEUE;
import static com.nxlh.manager.amqp.queue.ShopQueue.SHOP_SOLR_SAVE_QUEUE;

@Slf4j
@Component
public class ShopSolrAmqpService extends BaseAmqpService {


    @Autowired
    private ShopSolrRepository shopSolrRepository;

    @Autowired
    private LogService logService;

    private void logError(String title,Object obj){
        var log = new LogDTO();
        log.setId(IDUtils.genUUID());
        log.setAddtime(DateUtils.now());
        log.setLog(String.format("%s=========%s",title,obj));
        log.setLevel(LogLevelEnums.Level.fata.getValue());
        this.logService.add(log);
    }


    /**
     * 添加或修改索引
     */
    @RabbitListener(queues = SHOP_SOLR_SAVE_QUEUE)
    @RabbitHandler()
    private void addOrUpdate(ShopDTO shopDTO, Message message, Channel channel) throws IOException {
        try {
            this.log.info("添加/修改索引:{}",shopDTO);
            this.shopSolrRepository.addOrUpdate(shopDTO);
            this.ackOk(message,channel);
        } catch (Exception e) {
            this.log.error("添加/修改Shop索引失败!");
            this.logError("添加/修改Shop索引失败,"+e.toString(),shopDTO.getId());
            this.ackFail(message,channel);
            e.printStackTrace();
        }
    }



    @RabbitListener(queues = SHOP_SOLR_DELETE_QUEUE)
    @RabbitHandler()
    private void delete(String id,Message messgae,Channel channel) throws IOException {
        try {
            this.log.info("删除索引:{}",id);
            this.shopSolrRepository.removeById(id);
            this.ackOk(messgae,channel);
        } catch (Exception e) {
            this.log.error("删除Shop索引失败!");
            this.logError("删除Shop索引失败!"+e.toString(),id);
            this.ackFail(messgae,channel);
            e.printStackTrace();
        }
    }

}
