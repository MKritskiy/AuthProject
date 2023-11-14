package com.example.controllers;

import com.example.models.Book;
import com.example.models.Telephone;
import com.example.models.securityModels.User;
import com.example.repository.TelephoneRepository;
import com.example.security.RightChecker;
import com.example.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins="http://localhost:8080")
@RestController
@RequestMapping("/api")
@Slf4j
public class TelephoneController {
    @Autowired
    TelephoneRepository telephoneRepository;
    @Autowired
    private UserService userService;
    @GetMapping("/telephone")
    public ResponseEntity<List<Telephone>> getAllTelephones(){
        System.out.println("I am here");
        try{
            List<Telephone> telephones = new ArrayList<>();
            telephoneRepository.findAll().forEach(telephones::add);
            if (telephones.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(telephones, HttpStatus.OK);
        } catch( Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/telephone/{id}")
    public ResponseEntity<Telephone> getTelephoneById(@PathVariable Long id){
        Telephone telephone = telephoneRepository.findById(id).orElse(null);
        if (telephone!=null)
            return new ResponseEntity<>(telephone, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/telephone")
    public ResponseEntity<String> createTelephone(@RequestBody Telephone telephone){
        try{
            String sellerNumber = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.findByUsername(sellerNumber);
            List<String> userRoles = user.getAuthorities().stream().map(a->a.toString()).toList();
            log.info("IN createTelephone User roles: " + userRoles);
            log.info("IN createTelephone Telephone sellerNumber: " + telephone.getSellerNumber());
            if (!userRoles.contains("ROLE_ADMIN")){
                telephone.setSellerNumber(user.getId().intValue());
            } else if (telephone.getSellerNumber()==0)
                telephone.setSellerNumber(user.getId().intValue());
            telephoneRepository.save(telephone);
            return new ResponseEntity<>("New telephone was added", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/telephone/{id}")
    public ResponseEntity<String> updateTelephone(@PathVariable("id") Long id, @RequestBody Telephone telephone){
        Telephone _telephone = telephoneRepository.findById(id).orElse(null);
        if (_telephone!=null){
            _telephone = telephone;
            _telephone.setId(id);
            telephoneRepository.save(_telephone);
            return new ResponseEntity<>("Telephone was updated", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Cannot find telephone with id " + id, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/telephone/{id}")
    public ResponseEntity<String> deleteTelephone(@PathVariable("id") Long id){
        try{
            telephoneRepository.deleteById(id);
            return new ResponseEntity<>("Telephone was deleted successful", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Cannot delete telephone!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/telephone")
    public ResponseEntity<String> deleteAllTelephones(){
        try {
            telephoneRepository.deleteAll();
            return new ResponseEntity<>("Deleted all rows", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Cannot delete telephones!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
