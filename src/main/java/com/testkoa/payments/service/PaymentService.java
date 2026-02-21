package com.testkoa.payments.service;

import com.testkoa.payments.dto.PaymentRequestDTO;
import com.testkoa.payments.dto.PaymentResponseDTO;

import java.util.List;

/**
 * Servicio para gestión de pagos de créditos.
 */
public interface PaymentService {

    /**
     * Crea un nuevo pago.
     * 
     * @param request datos del pago a crear
     * @return el pago creado
     * @throws com.testkoa.payments.exception.DuplicatePaymentException si el pago ya existe
     */
    PaymentResponseDTO createPayment(PaymentRequestDTO request);

    /**
     * Obtiene todos los pagos de un crédito específico.
     * 
     * @param numeroCredito número del crédito
     * @return lista de pagos ordenados por fecha
     * @throws com.testkoa.payments.exception.PaymentNotFoundException si no hay pagos para el crédito
     */
    List<PaymentResponseDTO> getPaymentsByCreditNumber(String numeroCredito);
}
