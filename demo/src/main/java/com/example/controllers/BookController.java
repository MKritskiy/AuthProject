package com.example.controllers;

import com.example.models.Book;
import com.example.models.securityModels.User;
import com.example.repository.BookRepository;
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
public class BookController {


    private final BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    public BookController(BookRepository repository){
        bookRepository=repository;
    }
    @Autowired
    RightChecker rightChecker;

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBooks(){
        System.out.println("I am here");
        try{
            List<Book> books = new ArrayList<>();
            bookRepository.findAll().forEach(books::add);
            if (books.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch( Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        Book book = bookRepository.findById(id).orElse(null);
        if (book!=null)
            return new ResponseEntity<>(book, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/book")
    public ResponseEntity<String> createBook(@RequestBody Book book){
        try{
            String sellerNumber = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User user = userService.findByUsername(sellerNumber);
            List<String> userRoles = user.getAuthorities().stream().map(a->a.toString()).toList();
            if (!userRoles.contains("ROLE_ADMIN")){
                book.setSellerNumber(user.getId().intValue());
            } else if (book.getSellerNumber()==0)
                book.setSellerNumber(user.getId().intValue());

            bookRepository.save(book);
            return new ResponseEntity<>("New book was added", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/book/{id}")
    public ResponseEntity<String> updateBook(@PathVariable("id") Long id, @RequestBody Book book){
        Book _book = bookRepository.findById(id).orElse(null);
        if (_book!=null){
            _book.setId(id);
            _book.setAuthorName(book.getAuthorName());
            _book.setCost(book.getCost());
            _book.setSellerNumber(book.getSellerNumber());
            _book.setProductType(book.getProductType());
            _book.setName(book.getName());
            bookRepository.save(_book);
            return new ResponseEntity<>("Book was updated", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Cannot find book with id " + id, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id){
        try{
            bookRepository.deleteById(id);
            return new ResponseEntity<>("Took was deleted successful", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Cannot delete book!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/book")
    public ResponseEntity<String> deleteAllBooks(){
        try {
            bookRepository.deleteAll();
            return new ResponseEntity<>("Deleted all rows", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Cannot delete books!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
