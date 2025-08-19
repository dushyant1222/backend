package com.thesparknova.backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.thesparknova.backend.model.EventRegistration;
import com.thesparknova.backend.model.User;
import com.thesparknova.backend.repo.EventRegistrationRepository;
import com.thesparknova.backend.repo.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventRegistrationService {

    @Autowired private EventRegistrationRepository regRepo;
    @Autowired private UserRepository userRepo;

    @Value("${razorpay.key_id}") 
    private String keyId;
    @Value("${razorpay.key_secret}")
    private String keySecret;

    //1. Here we created the razorpay ka order and also the inital registration record
    public Order createRazorpayOrder(String userEmail, String eventId) throws RazorpayException {
        User user = userRepo.findByEmail(userEmail)
                            .orElseThrow(() -> new RuntimeException("User not found"));
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", 20000);    //I have used the fix 200rs apne according choose it
        options.put("currency", "INR");
        options.put("receipt", "rcpt_" + System.currentTimeMillis());

        Order order = client.orders.create(options);

        // Save the initial registration
        EventRegistration reg = new EventRegistration();
        reg.setEventId(eventId);
        reg.setUser(user);
        reg.setRegisteredAt(LocalDateTime.now());
        reg.setPaymentOrderId(order.get("id"));
        reg.setPaymentStatus("created");
        regRepo.save(reg);

        return order;
    }

    //2. Now this will update the payment on the successfull payment
    public void updatePayment(String orderId, String paymentId, String status) {
        EventRegistration reg = regRepo.findAll().stream()
            .filter(r -> orderId.equals(r.getPaymentOrderId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Registration not found for order: " + orderId));
        reg.setPaymentId(paymentId);
        reg.setPaymentStatus(status);
        regRepo.save(reg);
    }

    //3. This will get or say ki fetch the user registration
    public List<EventRegistration> getUserRegistrations(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                           .orElseThrow(() -> new RuntimeException("User not found"));
        return regRepo.findByUserOrderByRegisteredAtDesc(user);
    }
}
