package com.example.demo.controller;

import com.example.demo.document.ProfileDocument;
import com.example.demo.document.SessionDocument;
import com.example.demo.service.ProfileService;

import kafka.Consumer;
import kafka.KafkaConstant;
import kafka.Producer;
import com.example.demo.worker.Worker;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.elasticsearch.common.settings.Settings;

import static com.example.demo.util.Constant.DATA_INDEX;
import static com.example.demo.util.Constant.TYPE;

@CrossOrigin(origins = "http://localhost:8080")
@RestController()
public class ProfileController {
	private RestHighLevelClient client;
	private ProfileService service;
	private Producer producer;
//	private	Consumer consumer;
	private ObjectMapper mapper;
	private String a[] = new String[] {"test"};
	private List<String> consumerTopics = Arrays.asList(a); 
	private static final int POLL_WAIT_TIME = 1000;

    @Autowired
    public ProfileController(ProfileService service, RestHighLevelClient client) throws IOException{
    	this.client = client;
        this.service = service;
        this.producer = new Producer();
//        this.consumer = new Consumer(consumerTopics);
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
        mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
    }
 
    @GetMapping("/test")
    public String test(){

        return "Success";
    }

    @PostMapping("/all")
    @ResponseBody
    public void findAll(@RequestParam("name") String name, @RequestParam("location") String location, @RequestParam("keywords") String[] keywords, @RequestParam("platforms") String[] platforms, @RequestParam("numPage") String numPage, @RequestParam("file") MultipartFile file) throws Exception {
    	int sessionID = service.find_lastUsed(); 
    	//no data in index
    	if(sessionID == 0) {
    		sessionID = 1; 
    		service.add_sessionID(sessionID, name);
    	}
    	//maximum 5 sessions is stored
    	else if(sessionID == 5) {
    		service.update_lastUsed(sessionID);
    		service.delete_id(1);
    		sessionID = 1;
    		service.add_sessionID(sessionID, name);
    	}
    	//delete the previous last used 
    	else {
    		//shift the last used to current session
    		service.update_lastUsed(sessionID);
    		sessionID += 1;
    		service.delete_id(sessionID);
    		service.add_sessionID(sessionID, name);
    	}
//    	sessionID = 1;
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("id", "scrape");
    	resultMap.put("name", name);
    	resultMap.put("location", location);
    	resultMap.put("keywords", keywords);
    	resultMap.put("platforms", platforms);
    	resultMap.put("file", file.getBytes());
    	resultMap.put("sessionID", sessionID);
    	resultMap.put("numPage", Integer.parseInt(numPage));
    	System.out.println("name is: " + name);
    	System.out.println("location is: " + location);
        System.out.println("keyword is: " + keywords);
        System.out.println("platforms is: " + platforms);
        System.out.println("sessionID is: " + sessionID);
        System.out.println("numPage is: " + numPage);
        producer.sendMessage("test", mapper.writeValueAsString(resultMap));
        runConsumer();
        //service.add_sessionID(sessionID, name);
////        Consumer receive the message that scraping is done. 
//        while (true) {
//        	int flag = 0;
//        	System.out.println("in");
//        	ConsumerRecords<String, String> records = consumer.poll(POLL_WAIT_TIME);
//        	for (ConsumerRecord<String, String> record : records) {
//        		System.out.println(record.value());
//        		if (record.value().equals("done")){
//        			System.out.println("yes");
//        			flag = 1; 
//        			System.out.println(flag);
//        			break;
//        		}
//        	}
//        	if (flag == 1)
//        	break;
//        }
//        System.out.println("yes2");
    }
    
	public void runConsumer() throws Exception {
		Consumer consumer = new Consumer(consumerTopics);
	    while (true) {
	    	int flag = 0;
	    	System.out.println("in");
	    	ConsumerRecords<String, String> records = consumer.poll(POLL_WAIT_TIME);
	    	for (ConsumerRecord<String, String> record : records) {
	    		System.out.println(record.value());
	    		if (record.value().equals("done")){
	    			System.out.println("yes");
	    			flag = 1; 
	    			System.out.println(flag);
	    			break;
	    		}
	    	}
	    	if (flag == 1)
	    	break;
	    	//consumer.commit();
	    }
	consumer.close();
	}
    
    @GetMapping("/initTwitter")
    @ResponseBody
    public List<ProfileDocument> findAll_Twitter() throws Exception {
    	return service.findAll_Twitter();
    }
    
    @GetMapping("/initSession")
    @ResponseBody
    public List<SessionDocument> find_sessionID() throws Exception {
    	return service.find_sessionID();
    }
    
    @GetMapping("/initFacebook")
    @ResponseBody
    public List<ProfileDocument> findAll_Facebook() throws Exception {
    	return service.findAll_Facebook();
    }
}
