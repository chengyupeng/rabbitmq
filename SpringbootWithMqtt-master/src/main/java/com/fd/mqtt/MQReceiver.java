package com.fd.mqtt;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
	private static org.slf4j.Logger log=LoggerFactory.getLogger(MQReceiver.class); 
    /**
     * direct模式，交换机模式
     * @param messsage
     */
	@RabbitListener(queues=MQConfig.QUEUE)
  public void receive(String messsage){
	  log.info("------接收到------"+messsage);
  }
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
	public void receiveTopic1(String messsage){
		log.info("------topic-queue1 message------"+messsage);
	}
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
	public void receiveTopic2(String messsage){
		log.info("------topic-queue2 message------"+messsage);
	}
	@RabbitListener(queues=MQConfig.HEADER_QUEUE1)
	public void receiveHeaderQueue(byte[] messsage){
		log.info("------header-queue message------"+new String(messsage));
	}
}
