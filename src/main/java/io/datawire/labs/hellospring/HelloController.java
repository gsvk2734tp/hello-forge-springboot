package io.datawire.labs.hellospring;

import java.util.concurrent.TimeUnit;

import io.datawire.labs.hellospring.dao.PersonDAO;
import io.datawire.labs.hellospring.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @Autowired
    private PersonDAO personDAO;

    private static long start = System.currentTimeMillis();

    @GetMapping("/")
    public String sayHello() {
        long millis = System.currentTimeMillis() - start;
        String uptime = String.format("%02d:%02d",
                                      TimeUnit.MILLISECONDS.toMinutes(millis),
                                      TimeUnit.MILLISECONDS.toSeconds(millis) -
                                      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return String.format("Hello, Spring! (up %s)", uptime);
    }

    @ResponseBody
    @RequestMapping("/persons")
    public String index() {
        Iterable<Person> all = personDAO.findAll();

        StringBuilder sb = new StringBuilder();
        all.forEach(p -> sb.append(p.getFullName()).append("<br>"));

        return sb.toString();
    }

}
