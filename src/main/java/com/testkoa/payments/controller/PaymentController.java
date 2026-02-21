package com.testkoa.payments.controller;

import com.testkoa.payments.dto.PaymentRequestDTO;
import com.testkoa.payments.dto.PaymentResponseDTO;
import com.testkoa.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Crea un nuevo pago.
     * 
     * POST /payments
     * 
     * @param request datos del pago (numeroCredito, valor, fecha)
     * @return 201 Created con el pago creado
     *         400 Bad Request si los datos son inválidos
     *         409 Conflict si el pago ya existe
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO request) {
        log.debug("Recibida petición POST /payments: {}", request);
        PaymentResponseDTO response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene todos los pagos de un crédito específico.
     * 
     * GET /payments/{numeroCredito}
     * 
     * @param numeroCredito número del crédito a consultar
     * @return 200 OK con lista de pagos ordenados por fecha
     *         404 Not Found si no hay pagos para el crédito
     */
    @GetMapping("/{numeroCredito}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByCreditNumber(
            @PathVariable String numeroCredito) {
        log.debug("Recibida petición GET /payments/{}", numeroCredito);
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByCreditNumber(numeroCredito);
        return ResponseEntity.ok(payments);
    }
}
