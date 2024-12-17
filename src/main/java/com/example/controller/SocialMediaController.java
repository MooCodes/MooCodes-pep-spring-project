package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;

    private final MessageService messageService;

    @Autowired
    SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    ResponseEntity<Account> newAccount(@RequestBody Account newAccount) {
        String username = newAccount.getUsername();
        Account foundAccount = accountService.findByUsername(username);

        if (username.isBlank() || username.length() < 4 || foundAccount != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Account savedAccount = accountService.createAccount(newAccount);
        return ResponseEntity.ok(savedAccount);
    }

    @PostMapping("/login")
    ResponseEntity<Account> login(@RequestBody Account account) {
        Account foundAccount = accountService.findByUsername(account.getUsername());

        if (foundAccount != null && account.getUsername().equals(foundAccount.getUsername()) && account.getPassword().equals(foundAccount.getPassword())) {
            return ResponseEntity.ok(foundAccount);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/messages")
    ResponseEntity<Message> newMessage(@RequestBody Message message) {
        int userId = message.getPostedBy();
        
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255 || accountService.findById(userId) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Message savedMessage = messageService.createMessage(message);

        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable Integer messageId) {

        Optional<Message> message = messageService.getMessageById(messageId);
        if (message.isPresent()) {
            return ResponseEntity.ok(message.get());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        if (message.isPresent()) {
            messageService.deleteMessage(messageId);
            return ResponseEntity.ok(1);
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String, String> body) {
        Optional<Message> message = messageService.getMessageById(messageId);
        String messageText = body.get("messageText");
        if (message.isPresent()) {
            if (!messageText.isBlank() && messageText.length() <= 255) {
                System.out.println("messageText: " + messageText);
                System.out.println("!messageText.isBlank()" + !messageText.isBlank());
                messageService.updateMessage(messageId, messageText);
                return ResponseEntity.ok(1);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        System.out.println("hello");
        messages.forEach(msg -> {
            System.out.println(msg.getMessageText());
        });
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }
}
