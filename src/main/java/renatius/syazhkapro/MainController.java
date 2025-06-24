package renatius.syazhkapro;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class MainController {

    private final EmailService emailService;

    public MainController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/home")
    public String showHomePage(Model model){
        model.addAttribute("data", new Data());
        try {
            Path folder = Paths.get("uploads");
            List<String> images = Files.list(folder)
                    .filter(Files::isRegularFile)
                    .map(path -> "/images/" + path.getFileName().toString())
                    .toList();
            model.addAttribute("images", images);
        } catch (IOException e) {
            model.addAttribute("images", List.of()); // Пусто, если ошибка
        }
        return "FORDAD";
    }

    @PostMapping("/request")
    public String handleRequest(@Valid @ModelAttribute("data") Data data,
                                BindingResult bindingResult,
                                Model model){

        if (bindingResult.hasErrors()) {
            model.addAttribute("data", data);
            return "FORDAD";
        }
        else{
            try {
                emailService.sendRequestOnEmail(data.getPhoneNumber(), data.getContentMessage(), data.getSquare(), "shinkarevremstroy@gmail.com");
                return "redirect:/home";
            }catch (MessagingException e){
                System.out.println(e.getMessage());
            }
        }
        return "redirect:/home";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/"; // Локальная папка, куда сохранять
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            return ResponseEntity.ok("/images/" + filename); // URL для клиента
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки");
        }
    }
}
