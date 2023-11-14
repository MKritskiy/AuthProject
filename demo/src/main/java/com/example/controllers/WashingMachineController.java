package com.example.controllers;

import com.example.models.WashingMachine;
import com.example.models.securityModels.User;
import com.example.repository.WashingMachineRepository;
import com.example.security.RightChecker;
import com.example.services.UserService;
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
public class WashingMachineController {
    @Autowired
    WashingMachineRepository washingMachineRepository;
    @Autowired
    private UserService userService;



    @GetMapping("/washingMachine")
    public ResponseEntity<List<WashingMachine>> getAllWashingMachines(){
        System.out.println("I am here");
        try{
            List<WashingMachine> washingMachines = new ArrayList<>();
            washingMachineRepository.findAll().forEach(washingMachines::add);
            if (washingMachines.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(washingMachines, HttpStatus.OK);
        } catch( Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/washingMachine/{id}")
    public ResponseEntity<WashingMachine> getWashingMachineById(@PathVariable Long id){
        WashingMachine washingMachine = washingMachineRepository.findById(id).orElse(null);
        if (washingMachine!=null)
            return new ResponseEntity<>(washingMachine, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/washingMachine")
    public ResponseEntity<String> createWashingMachine(@RequestBody WashingMachine washingMachine){
        try{
            String sellerNumber = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.findByUsername(sellerNumber);
            List<String> userRoles = user.getAuthorities().stream().map(a->a.toString()).toList();
            if (!userRoles.contains("ROLE_ADMIN")){
                washingMachine.setSellerNumber(user.getId().intValue());
            } else if (washingMachine.getSellerNumber()==0)
                washingMachine.setSellerNumber(user.getId().intValue());
            washingMachineRepository.save(washingMachine);
            return new ResponseEntity<>("New washingMachine was added", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/washingMachine/{id}")
    public ResponseEntity<String> updateWashingMachine(@PathVariable("id") Long id, @RequestBody WashingMachine washingMachine){
        WashingMachine _washingMachine = washingMachineRepository.findById(id).orElse(null);
        if (_washingMachine!=null){
            _washingMachine = washingMachine;
            _washingMachine.setId(id);
            washingMachineRepository.save(_washingMachine);
            return new ResponseEntity<>("WashingMachine was updated", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Cannot find washingMachine with id " + id, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/washingMachine/{id}")
    public ResponseEntity<String> deleteWashingMachine(@PathVariable("id") Long id){
        try{
            washingMachineRepository.deleteById(id);
            return new ResponseEntity<>("WashingMachine was deleted successful", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Cannot delete washingMachine!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/washingMachine")
    public ResponseEntity<String> deleteAllWashingMachines(){
        try {
            washingMachineRepository.deleteAll();
            return new ResponseEntity<>("Deleted all rows", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Cannot delete washingMachines!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
