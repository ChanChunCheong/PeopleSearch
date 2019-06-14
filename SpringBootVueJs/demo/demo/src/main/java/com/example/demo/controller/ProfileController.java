package com.example.demo.controller;

import com.example.demo.document.ProfileDocument;
import com.example.demo.service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController()
public class ProfileController {
	private ProfileService service;

    @Autowired
    public ProfileController(ProfileService service) {

        this.service = service;
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
    public List<ProfileDocument> findAll() throws Exception {
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
