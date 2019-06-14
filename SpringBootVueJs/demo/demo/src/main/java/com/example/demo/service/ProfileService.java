package com.example.demo.service;

import com.example.demo.document.ProfileDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demo.util.Constant.INDEX;
import static com.example.demo.util.Constant.TYPE;

@Service
@Slf4j
public class ProfileService {


    private RestHighLevelClient client;


    private ObjectMapper objectMapper;

    @Autowired
    public ProfileService(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    /*
    public String createProfileDocument(ProfileDocument document) throws Exception {

        UUID uuid = UUID.randomUUID();
        document.setId(uuid.toString());

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, document.getId())
                .source(convertProfileDocumentToMap(document));

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        return indexResponse.getResult().name();
    }
	*/
    public ProfileDocument findById(String id) throws Exception {

            GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
            try {
                GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
                Map<String, Object> resultMap = getResponse.getSource();
                return convertMapToProfileDocument(resultMap);
            } catch (Exception e) {
                	System.out.println(e);
                    System.out.println("Id not found");
                return null;
            }
    }



   /*
    public String updateProfile(ProfileDocument document) throws Exception {

            ProfileDocument resultDocument = findById(document.getId());

            UpdateRequest updateRequest = new UpdateRequest(
                    INDEX,
                    TYPE,
                    resultDocument.getId());

            updateRequest.doc(convertProfileDocumentToMap(document));
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);

            return updateResponse
                    .getResult()
                    .name();

    }
	*/
    public List<ProfileDocument> findAll() throws Exception {

    	 try {
    		 SearchRequest searchRequest = buildSearchRequest(INDEX,TYPE);
    	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    	        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    	        searchSourceBuilder.size(58);
    	        searchRequest.source(searchSourceBuilder);
    	        SearchResponse searchResponse =
    	                client.search(searchRequest, RequestOptions.DEFAULT);
    	        return getSearchResult(searchResponse);
    	        /*
    	        List<ProfileDocument> pd = new ArrayList<ProfileDocument>(); 
    	        pd = getSearchResult(searchResponse);
    	        System.out.println(pd.get(1).getName());
    	        */
         } catch (ElasticsearchException e) {
             	System.out.println(e);
             return null;
         }
    }


    public List<ProfileDocument> findProfileByName(String name) throws Exception{


        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        searchRequest.types(TYPE);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MatchQueryBuilder matchQueryBuilder = QueryBuilders
                .matchQuery("name",name)
                .operator(Operator.AND);

        searchSourceBuilder.query(matchQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(searchResponse);

    }


    public String deleteProfileDocument(String id) throws Exception {

        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        DeleteResponse response = client.delete(deleteRequest,RequestOptions.DEFAULT);

        return response
                .getResult()
                .name();

    }

    private Map<String, Object> convertProfileDocumentToMap(ProfileDocument profileDocument) {
        return objectMapper.convertValue(profileDocument, Map.class);
    }

    private ProfileDocument convertMapToProfileDocument(Map<String, Object> map){
        return objectMapper.convertValue(map,ProfileDocument.class);
    }


    public List<ProfileDocument> searchByTechnology(String technology) throws Exception{

        SearchRequest searchRequest = buildSearchRequest(INDEX,TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders
                        .matchQuery("technologies.name",technology));

        searchSourceBuilder.query(QueryBuilders.nestedQuery("technologies",queryBuilder,ScoreMode.Avg));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);

        return getSearchResult(response);
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
}