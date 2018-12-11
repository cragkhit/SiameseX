package hello;

import crest.siamese.main.Siamese;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GreetingController {
    private final Siamese siamese;

    @Autowired
    public GreetingController(Siamese siamese) {
        this.siamese = siamese;
    }

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        String query = "public static void main(String[] args) { System.out.println(\"Hello World!\"); }";
        try {
            String result = this.siamese.queryWithString(greeting.getContent());
            System.out.println(result);
            greeting.setContent(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "result";
    }

}