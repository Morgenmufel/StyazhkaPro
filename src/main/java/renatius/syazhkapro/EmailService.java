package renatius.syazhkapro;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private final String EMAIL = "kola30503@gmail.com";

    public void sendRequestOnEmail(String phoneNumber, String contentMessage, int square, String toEmail) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Новая заявка на стяжку");
            helper.setText("Здравствуйте! Хотим заказать у вас звонок по поводу сухой стяжки!" +
                    " Вот наш номер телефона " + phoneNumber + ". " +
                    "Мы хотим рассчитать стоимость работ на площадь помещения " + square + "" +
                    " метров квадратных. Вот наше сообщение: " + contentMessage);

            mailSender.send(message);
            System.out.println("Письмо отправлено на " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}