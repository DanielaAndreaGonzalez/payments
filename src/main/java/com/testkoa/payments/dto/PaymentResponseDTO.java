package com.testkoa.payments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;
    
    private String numeroCredito;
    
    private BigDecimal valor;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
}
