package com.thesparknova.backend.controller;

import com.razorpay.Order;
import com.thesparknova.backend.service.EventRegistrationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/razorpay")
@CrossOrigin(origins = "http://localhost:3000")
public class RazorpayController {

    @Autowired private EventRegistrationService regSvc;

    @PostMapping(
      value    = "/createOrder",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, String> body) {
        try {
            String email   = body.get("email");
            String eventId = body.get("eventId");
            Order order    = regSvc.createRazorpayOrder(email, eventId);

            //yha converting the JSONObject to the plain Java map
            Map<String, Object> orderMap = order.toJson().toMap();
            return ResponseEntity.ok(orderMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    //Payment ke baad ki callback
    @PostMapping(value = "/paymentCallback",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> paymentCallback(@RequestBody Map<String, Object> body) {
        try {
            String orderId   = (String) body.get("order_id");
            String paymentId = (String) body.get("payment_id");
            String status    = (String) body.get("status");
            regSvc.updatePayment(orderId, paymentId, status);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Payment processing failed");
        }
    }
}
