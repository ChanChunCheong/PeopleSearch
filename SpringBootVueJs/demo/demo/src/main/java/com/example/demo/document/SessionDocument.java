package com.example.demo.document;

import com.example.demo.model.Technologies;
import lombok.Data;

import java.util.List;

@Data
public class SessionDocument {
    private String name;
    private String sessionID;
    
    public String getName(){
    	return this.name;
    }
    public int getsessionID(){
		int score = 0;
		if (this.sessionID != null) {
			score = Integer.parseInt(this.sessionID);
		}
		return score;
  	}   
}

