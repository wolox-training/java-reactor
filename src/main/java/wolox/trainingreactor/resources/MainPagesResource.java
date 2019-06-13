package wolox.trainingreactor.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainPagesResource {

    @GetMapping("/mainpages")
    public String getMainPage(){
        return "Mira tú app pinche lloron";
    }
}
