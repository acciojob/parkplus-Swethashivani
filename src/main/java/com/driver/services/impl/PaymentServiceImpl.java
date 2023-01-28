package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        boolean paymentModeValid = false;
        PaymentMode validPaymentMode = null;
        if ((reservation.getSpot().getPricePerHour() * reservation.getNumberOfHours()) <= amountSent) {
            for (PaymentMode paymentMode : PaymentMode.values()) {
                if (paymentMode.name().equals(mode.toUpperCase())) {
                    paymentModeValid = true;
                    validPaymentMode = paymentMode;
                    break;
                }
            }
            if (!paymentModeValid) {
                throw new Exception("Payment mode not detected");
            }else{
                Payment payment = new Payment(true, validPaymentMode);
                payment.setReservation(reservation);
                paymentRepository2.save(payment);
                return payment;
            }
        }else{
            throw new Exception("Insufficient Amount");
        }
    }
}
