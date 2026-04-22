package com.boky.PFE.restController;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.entite.Message;
import com.boky.PFE.exceptions.ChatAlreadyExistException;
import com.boky.PFE.exceptions.ChatNotFoundException;
import com.boky.PFE.exceptions.NoChatExistsInTheRepository;
import com.boky.PFE.service.ChatCreation;
import com.boky.PFE.service.ChatFinder;
import com.boky.PFE.service.ChatMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chats")
public class ChatRestController {

    @Autowired
    private ChatCreation chatCreation;

    @Autowired
    private ChatFinder chatFinder;

    @Autowired
    private ChatMessages chatMessages;

    @PostMapping("/add")
    public ResponseEntity<?> createChat(@RequestBody Chat chat) throws IOException {
        try {
            return new ResponseEntity<>(chatCreation.addChat(chat), HttpStatus.CREATED);
        } catch (ChatAlreadyExistException e) {
            return new ResponseEntity<>("Chat Already Exist", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/add/message1")
    public ResponseEntity<Message> addMessage2(@RequestBody Message message) throws IOException {
        return new ResponseEntity<>(chatCreation.addMessage2(message), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllChats() {
        try {
            return new ResponseEntity<>(chatFinder.findallchats(), HttpStatus.OK);
        } catch (NoChatExistsInTheRepository e) {
            return new ResponseEntity<>("List not found", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/all/messages/from/chat/{chatId}")
    public ResponseEntity<?> getAllMessagesInChat(@PathVariable int chatId) {
        try {
            List<Message> messageList = chatMessages.getAllMessagesInChat(chatId);
            return ResponseEntity.ok(messageList);
        } catch (NoChatExistsInTheRepository e) {
            return new ResponseEntity<>("Message List not found", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChatById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(chatFinder.getById(id), HttpStatus.OK);
        } catch (ChatNotFoundException e) {
            return new ResponseEntity<>("Chat Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/firstUserName/{username}")
    public ResponseEntity<?> getChatByFirstUserName(@PathVariable String username) {
        try {
            HashSet<Chat> byChat = chatFinder.getChatByFirstUserName(username);
            return new ResponseEntity<>(byChat, HttpStatus.OK);
        } catch (ChatNotFoundException e) {
            return new ResponseEntity<>("Chat Not Found", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/secondUserName/{username}")
    public ResponseEntity<?> getChatBySecondUserName(@PathVariable String username) {
        try {
            HashSet<Chat> byChat = chatFinder.getChatBySecondUserName(username);
            return new ResponseEntity<>(byChat, HttpStatus.OK);
        } catch (ChatNotFoundException e) {
            return new ResponseEntity<>("Chat Not Found", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getChatByFirstUserNameOrSecondUserName/{username}")
    public ResponseEntity<?> getChatByFirstUserNameOrSecondUserName(@PathVariable String username) {
        try {
            HashSet<Chat> byChat = chatFinder.getChatByFirstUserNameOrSecondUserName(username);
            return new ResponseEntity<>(byChat, HttpStatus.OK);
        } catch (ChatNotFoundException e) {
            return new ResponseEntity<>("Chat Not Found", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getChatByFirstUserNameAndSecondUserName")
    public ResponseEntity<?> getChatByFirstUserNameAndSecondUserName(
            @RequestParam("emailfirstUserName") String firstUserName,
            @RequestParam("emailSecondeUser") String secondUserName) {
        try {
            Chat chat = chatFinder.getChatByFirstUserNameAndSecondUserName(firstUserName, secondUserName);
            return new ResponseEntity<>(chat, HttpStatus.OK);
        } catch (ChatNotFoundException e) {
            return new ResponseEntity<>("Chat Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/message/{chatId}")
    public ResponseEntity<Chat> addMessage(@RequestBody Message add, @PathVariable int chatId)
            throws ChatNotFoundException {
        return new ResponseEntity<>(chatCreation.addMessage(add, chatId), HttpStatus.OK);
    }
}
