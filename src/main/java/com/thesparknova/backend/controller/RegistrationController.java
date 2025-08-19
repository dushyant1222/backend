package com.thesparknova.backend.controller;

import com.thesparknova.backend.model.EventRegistration;
import com.thesparknova.backend.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {

    @Autowired
    private EventRegistrationService registrationService;

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Map<String, Object>>> getUserRegistrations(@PathVariable String email) {
        try {
            List<EventRegistration> allRegs = registrationService.getUserRegistrations(email);
            List<Map<String, Object>> response = new ArrayList<>();

            //sirf successfull payments dikhayega ye
            for (EventRegistration reg : allRegs) {
                if ("paid".equals(reg.getPaymentStatus())) {  //only for thee paid registration 
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", reg.getId());
                    m.put("eventId", reg.getEventId());
                    m.put("eventName", "TheSparkNova Ideathon 2025");
                    m.put("eventDescription", "Innovation & Entrepreneurship Hackathon");
                    m.put("registeredAt", reg.getRegisteredAt().toString().substring(0, 10));
                    m.put("paymentStatus", reg.getPaymentStatus());
                    m.put("paymentId", reg.getPaymentId());
                    m.put("amount", "₹200");
                    m.put("prizePool", "₹1,00,000");
                    response.add(m);
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    //This method will chk ki if user is registered for that event ya nh
    @GetMapping("/check/{email}/{eventId}")
    public ResponseEntity<Map<String, Object>> checkRegistration(@PathVariable String email, @PathVariable String eventId) {
        try {
            List<EventRegistration> regs = registrationService.getUserRegistrations(email);
            boolean isRegistered = false;
            
            for (EventRegistration reg : regs) {
                if (eventId.equals(reg.getEventId()) && "paid".equals(reg.getPaymentStatus())) {
                    isRegistered = true;
                    break;
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("isRegistered", isRegistered);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
