package com.example.demo.worker;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
/*
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
*/
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import kafka.IWorker;
import kafka.KafkaConstant;
import kafka.Producer;
public class Worker implements IWorker {
	private static final String PLACES = "places";
	  private static final String ID = "profile_id";
	  private static final String HANDLE = "profile_handle";
	  private static final String LOCATION_FIELD = "location";
	  
	  private static final String CITY = "city";
	  private static final String REGION = "region";
	  private static final String COUNTRY = "country";
	  
	  private static final double SINGAPORE_LAT = 1.2930556;
	  private static final double SINGAPORE_LON = 103.8558333;

	  protected ConsumerRecords<String, String> consumerRecords;

	  private Producer producer = null;
	  /*
	  private ObjectMapper mapper;
	  private TransportClient esClient;
	  private String mapIndex = null;
	  
	  
	  public LocationWorker(Producer producer, TransportClient esClient, String mapIndex) {
	    this.producer = producer;
	    this.mapIndex = mapIndex;
	    this.esClient = esClient;

	    mapper = new ObjectMapper();
	    mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
	    mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
	    mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
	  }
	  */
	  public Worker(Producer producer) {
		  this.producer = producer;
	  }
	  @Override
	  public void setConsumerRecords(ConsumerRecords<String, String> records) {
	    this.consumerRecords = records;
	  }

	  @Override
	  public void run() {
		//producer.sendMessage("test", "Hello");
		
		for (ConsumerRecord<String, String> record : consumerRecords) {
			 System.out.println(record.value()); 
		}
		 
		System.out.println("yes");
		/*
	    for (ConsumerRecord<String, String> record : consumerRecords) {
	      processRecord(record);
	    }
		*/
	  }

	  /*
	  private void processRecord(ConsumerRecord<String, String> record) {
	    try {
	      switch(record.topic().toUpperCase()) {
	      case KafkaConstant.NETWORK_FACEBOOK_TARGET:
	        extractLocationFacebook(record.value());
	        break;
	      case KafkaConstant.NETWORK_FACEBOOK_SUSPECT:
	        extractLocationFacebook(record.value());
	        break;
	        //Skip all other types
	      default:
	        break;

	      }
	    } catch (Exception e) {
	      // catch any exception so that the thread does not die
	      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An exception occured when processing " + record.topic());
	      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage());
	      
	      e.printStackTrace();
	    }
	  }
	  */
}
