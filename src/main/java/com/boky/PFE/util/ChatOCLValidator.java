package com.boky.PFE.util;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.entite.Message;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.exceptions.OCLViolationException;
import com.boky.PFE.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Validator class that enforces OCL constraints defined in Chat.ocl.
 */
@Component
public class ChatOCLValidator {

    private final UtilisateurService utilisateurService;

    @Value("${ocl.postconditions.enabled:false}")
    private boolean postconditionsEnabled;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    public ChatOCLValidator(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // --- 1. CHAT INVARIANTS ---

    public void validateChat(Chat chat) {
        if (chat == null) return;

        // inv uniqueUsers: usernames must be valid and distinct
        if (chat.getFirstUserName() == null || chat.getSecondUserName() == null) {
            throw new OCLViolationException("uniqueUsers", "Usernames must not be null");
        }
        if (chat.getFirstUserName().equals(chat.getSecondUserName())) {
            throw new OCLViolationException("uniqueUsers", "Users must be different");
        }

        // inv validEmails: emails must be valid and distinct
        if (chat.getEmailfirstUserName() == null || chat.getEmailSecondeUser() == null) {
            throw new OCLViolationException("validEmails", "Emails must not be null");
        }
        if (chat.getEmailfirstUserName().equals(chat.getEmailSecondeUser())) {
            throw new OCLViolationException("uniqueUsers", "Emails must be different");
        }
    }

    public void validateChatUsersActive(Chat chat) {
        if (chat == null) return;

        // inv bothUsersMustBeActive
        checkUserActive(chat.getEmailfirstUserName(), "bothUsersMustBeActive");
        checkUserActive(chat.getEmailSecondeUser(), "bothUsersMustBeActive");
    }

    private void checkUserActive(String email, String constraintName) {
        Utilisateur user = utilisateurService.findByEmail(email);
        if (user == null || !user.isEtat()) {
            throw new OCLViolationException(constraintName, "User " + email + " must be active");
        }
    }

    // --- 2. MESSAGE INVARIANTS ---

    public void validateMessageFull(Message message) {
        validateMessage(message);
        validateMessageSenderActive(message);
    }

    public void validateMessage(Message message) {
        if (message == null) return;

        // inv messageBelongsToChat
        if (message.getChat() == null) {
            throw new OCLViolationException("messageBelongsToChat", "Message must belong to a Chat");
        }

        // inv validEmail
        if (message.getSenderEmail() == null || !EMAIL_PATTERN.matcher(message.getSenderEmail()).matches()) {
            throw new OCLViolationException("validEmail", "Invalid sender email format");
        }
    }

    public void validateMessageSenderActive(Message message) {
        if (message == null) return;

        // inv senderMustExistAndBeActive
        checkUserActive(message.getSenderEmail(), "senderMustExistAndBeActive");
    }

    // --- 3. PRE-CONDITIONS ---

    public void validateAddMessagePreconditions(Message msg) {
        // pre messageNotNull
        if (msg == null) {
            throw new OCLViolationException("messageNotNull", "Message must not be null");
        }

        // pre senderEmailValid
        if (msg.getSenderEmail() == null) {
            throw new OCLViolationException("senderEmailValid", "Sender email must not be null");
        }
    }

    // --- 4. POST-CONDITIONS ---

    public void validateAddMessagePostconditions(Chat chat, Message msg, int sizeBefore) {
        if (!postconditionsEnabled) return;

        List<Message> messages = chat.getMessageList();

        // post messageAdded
        if (!messages.contains(msg)) {
            throw new OCLViolationException("messageAdded", "Message list must include the added message");
        }

        // post sizeIncremented
        if (messages.size() != sizeBefore + 1) {
            throw new OCLViolationException("sizeIncremented", 
                String.format("Message list size must increase by 1 (expected %d, got %d)", sizeBefore + 1, messages.size()));
        }
    }
}
