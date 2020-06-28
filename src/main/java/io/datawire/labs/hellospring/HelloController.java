package io.datawire.labs.hellospring;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

import io.datawire.labs.hellospring.dao.PersonDAO;
import io.datawire.labs.hellospring.entity.Person;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @Autowired
    private PersonDAO personDAO;
    @Autowired
    AmqpTemplate template;

    private static long start = System.currentTimeMillis();
    private static final Logger logger = Logger.getLogger(HelloController.class);

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

    @RequestMapping("/emit")
    @ResponseBody
    String queue1() {
        logger.info("Emit to queue1");
        template.convertAndSend("queue1", sayHello());
        return "Emit to queue";
    }

}
