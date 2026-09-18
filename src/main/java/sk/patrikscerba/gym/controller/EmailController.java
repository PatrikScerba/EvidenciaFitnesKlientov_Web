package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sk.patrikscerba.gym.dto.email.EmailSendRequest;
import sk.patrikscerba.gym.service.email.NotificationEmailService;

import java.util.List;

/**
 * Controller pre odosielanie emailov.
 * Zabezpečuje prijatie požiadavky na odoslanie emailu,
 * validáciu vstupných dát
 * a odovzdanie požiadavky do service vrstvy.
 */
@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final NotificationEmailService notificationEmailService;

    public EmailController(
            NotificationEmailService notificationEmailService
    ) {
        this.notificationEmailService = notificationEmailService;
    }

    @PostMapping
    public ResponseEntity<String> sendEmail(
            @Valid @RequestPart("request") EmailSendRequest request,
            @RequestPart(value = "attachments", required = false)
            List<MultipartFile> attachments
    ) {
        notificationEmailService.sendEmail(request, attachments);

        return ResponseEntity.ok(
                "Email bol úspešne odoslaný."
        );
    }
}