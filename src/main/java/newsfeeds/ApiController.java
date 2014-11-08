package newsfeeds;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by samarth on 08/11/14.
 */
@Controller
@RequestMapping(value = {"v1"})
public class ApiController {
    @RequestMapping(value = {"/getAll"}, method = RequestMethod.GET)
    public void getAll(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/json");
        response.getWriter().write("{\"success\":\"Ok\"}");
    }
    @RequestMapping(value = {"/getTest"}, method = RequestMethod.GET)
    public void getTest(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/json");
        response.getWriter().write("{\"success\":\"dasldakdladkk\"}");
    }
}
