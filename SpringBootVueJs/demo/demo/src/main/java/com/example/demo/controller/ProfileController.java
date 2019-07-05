package com.example.demo.controller;

import com.example.demo.document.ProfileDocument;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.elasticsearch.common.settings.Settings;

@CrossOrigin(origins = "http://localhost:8080")
@RestController()
public class ProfileController {
	private ProfileService service;
	private Producer producer;
	private	Consumer consumer;
	private ObjectMapper mapper;
	//private Worker worker;
	private String a[] = new String[] {"test2"};
	private List<String> consumerTopics = Arrays.asList(a); 
	private static final int POLL_WAIT_TIME = 1000;

    @Autowired
    public ProfileController(ProfileService service) throws IOException{

        this.service = service;
        this.producer = new Producer();
        //this.worker = new Worker();
        this.consumer = new Consumer(consumerTopics);
        mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
        mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
    }

    @GetMapping("/test")
    public String test(){

        return "Success";
    }

    /*
    @PostMapping
    public ResponseEntity createProfile(@RequestBody ProfileDocument document) throws Exception {

        return new ResponseEntity(service.createProfileDocument(document), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateProfile(@RequestBody ProfileDocument document) throws Exception {

        return new ResponseEntity(service.updateProfile(document), HttpStatus.CREATED);
    }
	*/
    @GetMapping("/{id}")
    public ProfileDocument findById(@PathVariable String id) throws Exception {

        return service.findById(id);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<ProfileDocument> findAll(@RequestParam String name, @RequestParam String location, @RequestParam String keyword) throws Exception {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("id", "scrape");
    	resultMap.put("name", name);
    	resultMap.put("location", location);
    	resultMap.put("keyword", keyword);
    	System.out.println("name is: " + name);
        System.out.println("location is: " + location);
        System.out.println("keyword is: " + keyword);
        producer.sendMessage("test", mapper.writeValueAsString(resultMap));
        //Consumer receive the message that scraping is done. 
//        while (true) {
//        	int flag = 0;
//        	System.out.println("in");
//        	ConsumerRecords<String, String> records = consumer.poll(POLL_WAIT_TIME);
//        	for (ConsumerRecord<String, String> record : records) {
//        		System.out.println(record.value().equals("done"));
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
        System.out.println("yes2");
    	return service.findAll();
    }

    @GetMapping(value = "/search")
    public List<ProfileDocument> search(@RequestParam(value = "technology") String technology) throws Exception {
        return service.searchByTechnology(technology);
    }

    @GetMapping(value = "/api/v1/profiles/name-search")
    public List<ProfileDocument> searchByName(@RequestParam(value = "name") String name) throws Exception {
        return service.findProfileByName(name);
    }


    @DeleteMapping("/{id}")
    public String deleteProfileDocument(@PathVariable String id) throws Exception {

        return service.deleteProfileDocument(id);

    }
}
