package com.fd.mqtt;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class MQSender {
	private static org.slf4j.Logger log=LoggerFactory.getLogger(MQSender.class); 
	 @Autowired
     private AmqpTemplate amqpTemplate;
	
	public void sender(Object obj){
		String msg=MQSender.beanToString(obj);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}
	public void senderTopic(Object obj){
		String msg=MQSender.beanToString(obj);
		log.info("send topic message="+obj);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"lzc.message",msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"lzc.message1",msg+"2");
	}
	public void senderFanout(Object obj){
		String msg=MQSender.beanToString(obj);
		log.info("send Fanout message="+obj);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
	}
	public void senderHeader(Object obj){
		String msg=MQSender.beanToString(obj);
		log.info("send Header message="+obj);
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("header1", "value1");
		messageProperties.setHeader("header2", "value2");
		Message message=new Message(msg.getBytes(), messageProperties); 
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",message);
	}
	
	private static <T>  String beanToString(T value){
		if(value==null){
			return null;
		}
		Class<? extends Object> clazz = value.getClass();
		if(clazz==int.class||clazz==Integer.class){
			return ""+value;
		}else if(clazz==String.class){
			return (String)value;
		}else if(clazz==long.class||clazz==Long.class){
			return ""+value;
		}else{
			return JSON.toJSONString(value);
		}
		
	}
	
	public static <T> T stringToBean(String str,Class<T> clazz){
		
		if(str==null||str.length()<=0||clazz==null){
			return null;
		}else if(clazz==int.class||clazz==Integer.class){
			return (T)Integer.valueOf(str);
		}else if(clazz==String.class){
			return (T) str;
		}else if(clazz==long.class||clazz==Long.class){
			return (T) Long.valueOf(str);
		}else{
			return JSON.parseObject(str, clazz);
		}
		
		
	}
}
