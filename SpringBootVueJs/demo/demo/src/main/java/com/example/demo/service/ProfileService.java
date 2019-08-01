package com.example.demo.service;

import com.example.demo.document.ProfileDocument;
import com.example.demo.document.SessionDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.KafkaProducer;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import kafka.Consumer;
import kafka.KafkaConstant;
import kafka.Producer;
import com.example.demo.worker.Worker;

import static com.example.demo.util.Constant.DATA_INDEX;
import static com.example.demo.util.Constant.SESSION_INDEX;
import static com.example.demo.util.Constant.TYPE;
import static com.example.demo.util.Constant.SESSION_TYPE;


@Service
@Slf4j
public class ProfileService {
    private RestHighLevelClient client;
    private Producer producer;
    private ObjectMapper objectMapper;

    @Autowired
    public ProfileService(RestHighLevelClient client, ObjectMapper objectMapper) throws IOException{
        this.client = client;
        this.objectMapper = objectMapper;
        this.producer = new Producer(); 
    }

//    public ProfileDocument findById(String id) throws Exception {
//
//            GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
//            try {
//                GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//                Map<String, Object> resultMap = getResponse.getSource();
//                producer.sendMessage("test", "Hello");
//                return convertMapToProfileDocument(resultMap);
//            } catch (Exception e) {
//                	System.out.println(e);
//                    System.out.println("Id not found");
//                return null;
//            }
//    }

    public List<ProfileDocument> findAll(int sessionID) throws Exception {

   	 try {
   		 SearchRequest searchRequest = buildSearchRequest(DATA_INDEX,TYPE);
   	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
   	    	searchSourceBuilder.query(QueryBuilders.matchQuery("sessionID", Integer.toString(sessionID)));
   	        searchSourceBuilder.size(58);
   	        searchSourceBuilder.sort(new FieldSortBuilder("nameScore").order(SortOrder.DESC));
   	        searchRequest.source(searchSourceBuilder);
   	        SearchResponse searchResponse =
   	                client.search(searchRequest, RequestOptions.DEFAULT);
   	        return getSearchResult(searchResponse);
   	        
//   	        List<ProfileDocument> pd = new ArrayList<ProfileDocument>(); 
//   	        pd = getSearchResult(searchResponse);
//   	        System.out.println(pd.get(1).getbaselink());
//   	        return pd;
        } catch (ElasticsearchException e) {
            	System.out.println(e);
            return null;
        }
   }
    public List<ProfileDocument> findAll_Twitter() throws Exception {

    	 try {
    		 SearchRequest searchRequest = buildSearchRequest(DATA_INDEX,TYPE);
    	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    	        BoolQueryBuilder query = new BoolQueryBuilder();
    	        query.must(new MatchQueryBuilder("platform", "Twitter"));
//    	        query.must(new MatchQueryBuilder("lastUsed", "yes"));
    	        searchSourceBuilder.query(query);
    	        searchSourceBuilder.size(1000);
    	        searchSourceBuilder.sort(new FieldSortBuilder("totalScore").order(SortOrder.DESC));
    	        searchRequest.source(searchSourceBuilder);
    	        SearchResponse searchResponse =
    	                client.search(searchRequest, RequestOptions.DEFAULT);
    	        return getSearchResult(searchResponse);
    	        
//    	        List<ProfileDocument> pd = new ArrayList<ProfileDocument>(); 
//    	        pd = getSearchResult(searchResponse);
//    	        System.out.println(pd.get(1).getbaselink());
//    	        return pd;
         } catch (ElasticsearchException e) {
             	System.out.println(e);
             return null;
         }
    }
    
    public List<ProfileDocument> findAll_Facebook() throws Exception {

   	 try {
   		 SearchRequest searchRequest = buildSearchRequest(DATA_INDEX,TYPE);
   	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
   	        BoolQueryBuilder query = new BoolQueryBuilder();
	        query.must(new MatchQueryBuilder("platform", "Facebook"));
//	        query.must(new MatchQueryBuilder("lastUsed", "yes"));
	        searchSourceBuilder.query(query);
   	        searchSourceBuilder.size(1000);
   	        searchSourceBuilder.sort(new FieldSortBuilder("totalScore").order(SortOrder.DESC));
   	        searchRequest.source(searchSourceBuilder);
   	        SearchResponse searchResponse =
   	                client.search(searchRequest, RequestOptions.DEFAULT);
//   	        return getSearchResult(searchResponse);
   	        
   	        List<ProfileDocument> pd = new ArrayList<ProfileDocument>(); 
   	        pd = getSearchResult(searchResponse);
   	        return pd;
        } catch (ElasticsearchException e) {
            	System.out.println(e);
            return null;
        }
   }
    
    public List<SessionDocument> find_sessionID() throws Exception {

      	 try {
      		 SearchRequest searchRequest = buildSearchRequest(SESSION_INDEX,SESSION_TYPE);
      	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      	        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
      	        searchSourceBuilder.size(10);
      	        searchRequest.source(searchSourceBuilder);
      	        SearchResponse searchResponse =
      	                client.search(searchRequest, RequestOptions.DEFAULT);
      	        List<SessionDocument> pd = new ArrayList<SessionDocument>(); 
      	        pd = getSearchResult2(searchResponse);
      	        System.out.println(pd);
      	        return pd;
           } catch (ElasticsearchException e) {
               	System.out.println(e);
               return null;
           }
      }
    
    private List<SessionDocument> getSearchResult2(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<SessionDocument> profileDocuments = new ArrayList<>();

        for (SearchHit hit : searchHit){
            profileDocuments
                    .add(objectMapper
                            .convertValue(hit
                                    .getSourceAsMap(),SessionDocument.class));
        }

        return profileDocuments;
    }
    
    
    private List<ProfileDocument> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<ProfileDocument> profileDocuments = new ArrayList<>();

        for (SearchHit hit : searchHit){
            profileDocuments
                    .add(objectMapper
                            .convertValue(hit
                                    .getSourceAsMap(), ProfileDocument.class));
        }

        return profileDocuments;
    }
    

    private SearchRequest buildSearchRequest(String index, String type) {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);

        return searchRequest;
    }
    
    public int find_lastUsed() throws Exception {
      	 try {
      		 SearchRequest searchRequest = buildSearchRequest(DATA_INDEX,TYPE);
      	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      	        searchSourceBuilder.query(QueryBuilders.matchQuery("lastUsed", "yes"));
      	        searchRequest.source(searchSourceBuilder);
      	        SearchResponse searchResponse =
      	                client.search(searchRequest, RequestOptions.DEFAULT);
//      	        return getSearchResult(searchResponse);
      	        
      	        List<ProfileDocument> pd = new ArrayList<ProfileDocument>(); 
      	        pd = getSearchResult(searchResponse);
      	        if(pd.size() > 0) { 
	      	        System.out.println("here");
	      	        return pd.get(1).getsessionID();
      	        }
      	        else {
      	        	return 0;
      	        }
           } catch (ElasticsearchException e) {
               	System.out.println(e);
               return 0;
           }
      }
    
    public void update_lastUsed(int id) throws Exception{
    	try {
    		Map<String, Object> jsonMap = new HashMap<>();
    		jsonMap.put("lastUsed", "no");
    		UpdateByQueryRequest request = new UpdateByQueryRequest(DATA_INDEX);
    		Script script = new Script("ctx._source.lastUsed = 'no'");
    		request.setQuery(new TermQueryBuilder("sessionID", Integer.toString(id)));
    		request.setScript(script);
    	    client.updateByQuery(request, RequestOptions.DEFAULT);
    	} catch (ElasticsearchException exception) {
    	    if (exception.status() == RestStatus.NOT_FOUND) {
    	    	System.out.println("no id found");
    	    }
    	}
    }
    public void delete_id(int id) throws Exception {
    	try {
    		DeleteByQueryRequest request = new DeleteByQueryRequest(DATA_INDEX);
    		request.setQuery(new TermQueryBuilder("sessionID", Integer.toString(id)));
    		DeleteByQueryRequest request2 = new DeleteByQueryRequest(SESSION_INDEX);
    		request2.setQuery(new TermQueryBuilder("sessionID", Integer.toString(id)));
    	    client.deleteByQuery(request, RequestOptions.DEFAULT);
    	    client.deleteByQuery(request2, RequestOptions.DEFAULT);
    	} catch (ElasticsearchException exception) {
    	    if (exception.status() == RestStatus.NOT_FOUND) {
    	    	System.out.println("no id found");
    	    }
    	}
     }
    public void add_sessionID(int id, String name) throws Exception {
    	try {
    		Map<String, Object> jsonMap = new HashMap<>();
    		jsonMap.put("name", name);
    		jsonMap.put("sessionID", Integer.toString(id));
    		IndexRequest indexRequest = new IndexRequest(SESSION_INDEX)
    		    .source(jsonMap);
    		client.index(indexRequest, RequestOptions.DEFAULT);
    		System.out.println("indexed");
    	} catch (ElasticsearchException e) {
    		System.out.println(e);
    	}
     }
}