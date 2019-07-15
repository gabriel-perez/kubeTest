package org.gdps.kubeTest;

import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class TestKube {

    @RequestMapping("/")
    public String Test() {

        return "Testing kube";
    }

    @RequestMapping("/gabriel")
    public String TestGabriel() {

        return "Testing gabriel path";
    }
}
