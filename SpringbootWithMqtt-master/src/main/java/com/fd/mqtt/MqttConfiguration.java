package com.fd.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by qasim on 29/12/15.
 */
@Configuration
public class MqttConfiguration {
	 @Value("${mqtt.broker.serverUri}")
	 private String serviceUri;
    @Bean
    public MqttTemplate mqttTemplate() throws MqttException {
        MqttTemplate mqttTemplate = new MqttTemplate(serviceUri);
        mqttTemplate.connect(MqttTemplate.defaultOptions());
        System.out.println("Mqtt connected with mqtt broker at url : "+serviceUri);
        return mqttTemplate;
    }
}
