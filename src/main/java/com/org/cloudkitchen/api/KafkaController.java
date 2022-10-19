//package com.org.cloudkitchen.api;
//
//import com.org.cloudkitchen.service.KafkaProducer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/kafka")
//public class KafkaController {
//    private final KafkaProducer kafkaProducer;
//
//    @Autowired
//    public KafkaController(KafkaProducer kafkaProducer) {
//        this.kafkaProducer = kafkaProducer;
//    }
//
//    @GetMapping("/message")
//    public String sendMessage(@RequestParam("message") String message) {
//        this.kafkaProducer.sendMessage(message);
//
//        return "success";
//    }
//}
