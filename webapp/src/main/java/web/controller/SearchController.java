package web.controller;

import crest.siamese.main.Siamese;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import web.model.Search;

import java.security.Principal;

@Controller
public class SearchController implements ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final Siamese siamese;

    @Autowired
    public SearchController(Siamese siamese) {
        this.siamese = siamese;
    }

    @GetMapping("/search")
    public String searchForm(Model model, Principal principal) {
        logger.info("user: {}", principal.getName());

        model.addAttribute("search", new Search());
        return "search";
    }

    @PostMapping("/search")
    public String searchSubmit(@ModelAttribute Search search) {
        logger.info("search length: {}", search.getContent().length());

        JSONObject resultJSON = queryResultJSON(search.getContent());
        search.setResult(resultJSON);
        return "result";
    }

    @PostMapping(path = "/api/searchJSON", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public JSONObject searchSubmitJSON(@RequestBody Search search) {
        logger.info("search length: {}", search.getContent().length());

        JSONObject resultJSON = queryResultJSON(search.getContent());
        return resultJSON;
    }

    private JSONObject queryResultJSON(String searchString) {
        JSONObject resultJSON = new JSONObject();

        try {
            String result = this.siamese.queryWithString(searchString);

            // TODO: Make sure result returned from siamese is JSON!
            // Fornow, check it in parse exception
            resultJSON = parseStringToJSON(result);

            // TODO: What to Log from the result?
            // Placeholder, clones quantity
            // Current version 9 Mar 19, return clones: [[ { }, { }, { }, ... ]]
            // Comment from Chaiyong mentioned that it should be [ { }, { }, { }, ... ]
            JSONArray clones = (JSONArray) resultJSON.get("clones");
            logger.info("query result: found {} duplications", clones.size());
        } catch (Exception e) {
            logger.error("exception: {}", ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
        return resultJSON;
    }

    private JSONObject parseStringToJSON(String stringToParse) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(stringToParse);
        } catch (ParseException e) {
            logger.error("exception: {}", ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
        return jsonObject;
    }

    // By default policy, any error in spring boot app will route to /error
    // Implementing ErrorController will override the routing behaviour
    // forward: will allow redirecting a new page rather than /error, note that the page must be in resource
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "forward:/login.html";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
