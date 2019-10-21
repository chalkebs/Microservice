package com.fundoo.elasticSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundoo.user.model.NoteEntity;
import com.fundoo.utility.TokenGenerator;

@Service
public class ElasticSearchDao 
{
	@Autowired
	private TokenGenerator tokenUtil;
	
	private final String INDEX = "loginregistration";
	private final String TYPE = "noteentity";  
	private RestHighLevelClient restHighLevelClient;
	private ObjectMapper objectMapper;

	public ElasticSearchDao( ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) 
	{
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	public IndexResponse insertNote(NoteEntity noteEntity, String token) 
	{
		IndexResponse response = null;
		
		@SuppressWarnings("rawtypes")
		Map dataMap = objectMapper.convertValue(noteEntity, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, String.valueOf(noteEntity.getNoteId()))
				.source(dataMap);
		try 
		{
			return response = restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
		} 
		catch(ElasticsearchException e) 
		{
			e.getDetailedMessage();
		} 
		catch (java.io.IOException ex)
		{
			ex.getLocalizedMessage();
		}
		return response;
	}

	public List<NoteEntity> searchNote(String token, String searchField) throws IOException 
	{
		Long id = tokenUtil.decodeToken(token);
		
		System.out.println("ElasticSearchDao.searchNote()");
		
		SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        
        System.out.println(searchField);
        
        searchSourceBuilder.query(QueryBuilders.boolQuery()
        				.must(QueryBuilders.termQuery("userEntityId", id))
        				.must(QueryBuilders.queryStringQuery("*" + searchField + "*")
                        .field("noteTitle")
                        .field("noteData")));
        
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                
        return searchResult(response);
	}

	private List<NoteEntity> searchResult(SearchResponse response) 
	{
        SearchHit[] searchHit = response.getHits().getHits();

        List<NoteEntity> notes = new ArrayList<>();

        if (searchHit.length > 0) 
        {
            Arrays.stream(searchHit).forEach(hit -> notes.add(objectMapper.convertValue(hit.getSourceAsMap(), 
            		NoteEntity.class)));
        }
        
        return notes;
    }
	

	public Map<String, Object> updateNote(Long noteId, NoteEntity noteEntity, String token) 
	{
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, noteId.toString())
				.fetchSource(true); // Fetch Object after its update
		
		try 
		{
			String noteJson = objectMapper.writeValueAsString(noteEntity);
			updateRequest.doc(noteJson, XContentType.JSON);
			
			UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
			
			Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();			
			return sourceAsMap;
		}
		catch (JsonProcessingException e)
		{
			e.getMessage();
			return null;
		} 
		catch (java.io.IOException e)
		{
			e.getLocalizedMessage();
			return null;
		}
	}

	public DeleteResponse deleteNote(Long noteId, String token) 
	{
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, noteId.toString());
		DeleteResponse deleteResponse = null;
		try 
		{
		    deleteResponse = restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
		} 
		catch (java.io.IOException e)
		{
		    e.getLocalizedMessage();
		}
		
		return deleteResponse;
	}
	
}
