package com.example.demo.document;

import com.example.demo.model.Technologies;
import lombok.Data;

import java.util.List;

@Data
public class ProfileDocument {
    private String dateJoined;
    private String name;
    private String link; 
    private String description;
    private String location;
    
    public String getName(){
    	return this.name;
    }
    public String getdateJoined(){
    	return this.dateJoined;
    }
    public String getLink(){
    	return this.link;
    }
    public String getDescription(){
    	return this.description;
    }
    public String getLocation(){
    	return this.location;
    }
}

