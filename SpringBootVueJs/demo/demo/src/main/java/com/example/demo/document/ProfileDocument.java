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
    private String imgScore;
    private String description;
    private String location;
    private String locScore;
    private String wordScore;
    private String platform;
    private String[] image_urls;
    private String totalScore;
    private String lastUsed;
    private String sessionID;
    private String education;
    private String work;
    
    public String getName(){
    	return this.name;
    }
    public String getdateJoined(){
    	return this.dateJoined;
    }
//    public int getnameScore(){
//    	int score = 0;
//    	if (this.nameScore != null) {
//    		score = Integer.parseInt(this.nameScore);
//    	}
//    	return score;
//    }
    public String getnameScore(){
    	return this.nameScore;
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
    public String[] getimage_urls(){
    	return this.image_urls;
    }
//    public int getimgScore(){
//    	int score = 0;
//    	if (this.imgScore != null) {
//    		score = Integer.parseInt(this.imgScore);
//    	}
//    	return score;
//    }
//    public int getlocScore(){
//    	int score = 0;
//    	if (this.locScore != null) {
//    		score = Integer.parseInt(this.locScore);
//    	}
//    	return score;
//    }
//    public int getwordScore(){
//    	int score = 0;
//    	if (this.wordScore != null) {
//    		score = Integer.parseInt(this.wordScore);
//    	}
//    	return score;
//    }
    public String gettotalScore(){
    	return this.totalScore;
    }
    public int getsessionID(){
		int score = 0;
		if (this.sessionID != null) {
			score = Integer.parseInt(this.sessionID);
		}
		return score;
  	}   
    public String getPlatform(){
    	return this.platform;
    }
    public String getlastUsed(){
    	return this.platform;
    }
    public String geteducation(){
    	return this.education;
    }
    public String getwork(){
    	return this.work;
    }
}

