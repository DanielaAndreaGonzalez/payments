package com.testkoa.payments.repository;

import com.testkoa.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Busca todos los pagos de un crédito específico, ordenados por fecha ascendente.
     * 
     * @param numeroCredito el número de crédito a buscar
     * @return lista de pagos ordenados por fecha
     */
    List<Payment> findByNumeroCreditoOrderByFechaAsc(String numeroCredito);
}
