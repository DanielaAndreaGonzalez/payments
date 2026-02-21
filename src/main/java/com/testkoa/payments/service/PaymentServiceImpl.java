package com.testkoa.payments.service;

import com.testkoa.payments.dto.PaymentRequestDTO;
import com.testkoa.payments.dto.PaymentResponseDTO;
import com.testkoa.payments.entity.Payment;
import com.testkoa.payments.exception.DuplicatePaymentException;
import com.testkoa.payments.exception.PaymentNotFoundException;
import com.testkoa.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        log.info("Creando pago para crédito {} por valor {} en fecha {}", 
                 request.getNumeroCredito(), request.getValor(), request.getFecha());
        
        try {
            // Convertir DTO a entidad
            Payment payment = Payment.builder()
                .numeroCredito(request.getNumeroCredito())
                .valor(request.getValor())
                .fecha(request.getFecha())
                .build();
            
            // Guardar en base de datos
            Payment savedPayment = paymentRepository.save(payment);
            
            log.info("Pago creado exitosamente con ID: {}", savedPayment.getId());
            
            // Convertir entidad a DTO de respuesta
            return convertToResponseDTO(savedPayment);
            
        } catch (DataIntegrityViolationException ex) {
            log.warn("Intento de crear pago duplicado: crédito={}, fecha={}, valor={}", 
                     request.getNumeroCredito(), request.getFecha(), request.getValor());
            throw new DuplicatePaymentException(
                "Ya existe un pago con los mismos datos (número de crédito, fecha y valor)", ex
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByCreditNumber(String numeroCredito) {
        log.info("Consultando pagos para crédito: {}", numeroCredito);
        
        List<Payment> payments = paymentRepository.findByNumeroCreditoOrderByFechaAsc(numeroCredito);
        
        if (payments.isEmpty()) {
            log.warn("No se encontraron pagos para el crédito: {}", numeroCredito);
            throw new PaymentNotFoundException(
                "No se encontraron pagos para el crédito: " + numeroCredito
            );
        }
        
        log.info("Se encontraron {} pagos para el crédito: {}", payments.size(), numeroCredito);
        
        return payments.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Payment a PaymentResponseDTO.
     */
    private PaymentResponseDTO convertToResponseDTO(Payment payment) {
        return PaymentResponseDTO.builder()
            .id(payment.getId())
            .numeroCredito(payment.getNumeroCredito())
            .valor(payment.getValor())
            .fecha(payment.getFecha())
            .build();
    }
}
