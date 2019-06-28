package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.elasticsearch.common.settings.Settings;

//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import kafka.Consumer;
import kafka.KafkaConstant;
import kafka.Producer;
import com.example.demo.worker.Worker;

@SpringBootApplication
public class DemoApplication {
	private static final String PRODUCER_PROPERTIES_FILE = "config/producer.properties";
	private static final String CONSUMER_PROPERTIES_FILE = "config/consumer.properties";
	private static final String DB_PROPERTIES_FILE = "config/database.properties";

	public static void main(String[] args) throws IOException{
		SpringApplication.run(DemoApplication.class, args);
		
//		Properties producerProp = new Properties();
//	    Properties consumerProp = new Properties();
//	    Properties dbProperties = new Properties();
//	    InputStream stream = null;
//
//	    try {
//	      stream = new FileInputStream(PRODUCER_PROPERTIES_FILE);
//	      producerProp.load(stream);
//	      Producer producer = new Producer(producerProp);
//
//	      // Create Consumer
//	      stream = new FileInputStream(CONSUMER_PROPERTIES_FILE);
//	      consumerProp.load(stream);
//
//	      stream = new FileInputStream(DB_PROPERTIES_FILE);
//	      dbProperties.load(stream);
//	      
//	      String a[] = new String[] { "test", "test2"};
//	      List<String> consumerTopics = Arrays.asList(a); 
//	      System.out.println("The list is: " + consumerTopics); 
//	      
//	      
//          Consumer consumer = new Consumer(consumerProp, consumerTopics);
//          Worker worker = new Worker (producer);
//          producer.sendMessage("test", "Hello");
//          consumer.runConsumerPollLoop(worker);
//          
//          //LocationWorker worker = new LocationWorker(producer, esClient, mapIndex);
//          //process consumer records and produce another record with topics 
//        } 
//	    finally {
//          stream.close();
//        }
      }
      
}
