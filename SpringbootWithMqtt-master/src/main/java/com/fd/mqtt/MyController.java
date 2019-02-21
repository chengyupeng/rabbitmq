package com.fd.mqtt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by qasim on 29/12/15.
 */

@Controller
@RequestMapping("/send")
public class MyController {

    @Autowired
    MqttTemplate mqttTemplate;
    
    @Autowired
    private MQSender mqSender;
    
    
    
    
    private static final String SEND_MESSAGE = "sendMessage";
    /**
     * 获取 Book 列表
     * 处理 "/book" 的 GET 请求，用来获取 Book 列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap map) {
    	map.addAttribute("action", "create");
        return SEND_MESSAGE;
    }

    @ResponseBody
    @RequestMapping(value="/mq",method = RequestMethod.GET)
    public String mq(){
    	mqSender.sender("hello immoc");
    	return "success";
    }
   
    @ResponseBody
    @RequestMapping(value="/mq/topic",method = RequestMethod.GET)
    public String topic(){
    	mqSender.senderTopic("hello immoc");
    	return "success";
    }
    
    @ResponseBody
    @RequestMapping(value="/mq/fanout",method = RequestMethod.GET)
    public String fanout(){
    	mqSender.senderFanout("hello immoc");
    	return "success";
    }
    @ResponseBody
    @RequestMapping(value="/mq/header",method = RequestMethod.GET)
    public String header(){
    	mqSender.senderHeader("hello immoc");
    	return "success";
    }
    


    @ResponseBody
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public String testService(String name) throws MqttException {
        MqttMessage message = new MqttMessage(name.getBytes());
        message.setQos(Constants.FD_MQTT_QOS);
        message.setRetained(true);
        mqttTemplate.publish(Constants.FD_MQTT_TOPIC, message);
        return "success";
    }
    
    @ResponseBody
    @RequestMapping(value="/upload",method = RequestMethod.GET)
    public String upload(String fileName,HttpServletRequest request,HttpServletResponse response) throws MqttException, IOException {
    	String flag = "1";
 		response.setContentType("text/html;charset=UTF-8");
 		OutputStream outputStream = response.getOutputStream();
		 HttpURLConnection conn = null;
		 InputStream inputStream = null;
		 BufferedOutputStream bos=null;
		 BufferedInputStream bis=null;
		 
		 try
		{
			 // 建立链接
			 URL httpUrl=new URL("https://oby0yx23h.qnssl.com/aop-bfw-3.0.0.zip");
			 conn=(HttpURLConnection) httpUrl.openConnection();
			 //以Post方式提交表单，默认get方式
			 conn.setRequestMethod("GET");
			 conn.setConnectTimeout(20000);
			 
			 conn.setDoOutput(true);
			 conn.setDoInput(true);
			 conn.setUseCaches(false);
			 conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
			 conn.setRequestMethod("POST");
			 conn.setRequestProperty("connection", "Keep-Alive");
			 conn.setRequestProperty("Charsert", "UTF-8");
			 conn.setConnectTimeout(60000);
		    conn.setReadTimeout(60000);
		     conn.setDoInput(true);  
		     conn.setDoOutput(true);
		     conn.setRequestProperty("connection", "Keep-Alive");
		     // post方式不能使用缓存 
		     conn.setUseCaches(false);
		     //连接指定的资源 
		     conn.connect();
		     //获取网络输入流
		     inputStream=conn.getInputStream();
		     bis= new BufferedInputStream(inputStream);
		     //判断文件的保存路径后面是否以/结尾
		     //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
	          bos= new BufferedOutputStream(outputStream);
	         response.setContentType("application/octet-stream");
	 			response.setCharacterEncoding("UTF-8");
	 			response.setHeader("Content-Disposition",
	 					"attachment; filename=" + new String(fileName.getBytes("gbk"), "iso-8859-1"));
	         byte[] buf = new byte[4096];
	         int length = bis.read(buf);
	         //保存文件
	         while((length=bis.read(buf)) != -1)
	         {
	        	 bos.write(buf, 0, length);
	        	 length = bis.read(buf);
	         }
	 		/*IOUtils.copy(inputStream,outputStream);*/
	         
		} catch (Exception e)
		{flag = "2";
			 e.printStackTrace();
			 System.out.println("抛出异常！！");
		}finally {
			if(bos!=null) {
				bos.close();
			}
			if(bis!=null) {
				bis.close();
			}
			if(conn!=null)
			conn.disconnect();
		}
		 
		 return flag;
    }
}
