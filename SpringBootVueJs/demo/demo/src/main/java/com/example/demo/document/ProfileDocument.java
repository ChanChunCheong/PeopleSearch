package com.example.demo.document;

import com.example.demo.model.Technologies;
import lombok.Data;

import java.util.List;

@Data
public class ProfileDocument {
    private String dateJoined;
    private String name;
    private String nameScore;
    private String link;
    private String baselink; 
    private String imgSrc; 
    private String description;
    private String location;
    
    public String getName(){
    	return this.name;
    }
    public String getdateJoined(){
    	return this.dateJoined;
    }
    public int getnameScore(){
    	int score = 0;
    	if (this.nameScore != null) {
    		score = Integer.parseInt(this.nameScore);
    	}
    	return score;
    }
    public String getLink(){
    	return this.link;
    }
    public String getbaselink(){
    	return this.baselink;
    }
    public String getimgSrc(){
    	return this.imgSrc;
    }
    public String getDescription(){
    	return this.description;
    }
    public String getLocation(){
    	return this.location;
    }
}

