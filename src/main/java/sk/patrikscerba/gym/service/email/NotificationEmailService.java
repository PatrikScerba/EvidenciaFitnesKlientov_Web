package sk.patrikscerba.gym.service.email;

import org.springframework.web.multipart.MultipartFile;
import sk.patrikscerba.gym.dto.email.EmailSendRequest;

import java.util.List;

public interface NotificationEmailService {

    void sendEmail(EmailSendRequest request,
                   List<MultipartFile> attachments);
}

