package sk.patrikscerba.gym.service.email;

import org.springframework.web.multipart.MultipartFile;
import sk.patrikscerba.gym.dto.email.EmailSendRequest;
import sk.patrikscerba.gym.dto.email.EmailSendResult;

import java.util.List;

public interface NotificationEmailService {

    EmailSendResult sendEmail(EmailSendRequest request,
                              List<MultipartFile> attachments);
}

