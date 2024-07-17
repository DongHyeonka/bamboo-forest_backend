package org.jungppo.bambooforest.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jungppo.bambooforest.payment.domain.entity.PaymentEntity;
import org.jungppo.bambooforest.payment.domain.entity.PaymentStatusType;

@Getter
@RequiredArgsConstructor
public class PaymentDto {
    private final UUID id;
    private final PaymentStatusType status;
    private final String provider;
    private final BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    public static PaymentDto from(final PaymentEntity paymentEntity) {
        return new PaymentDto(
                paymentEntity.getId(),
                paymentEntity.getStatus(),
                paymentEntity.getProvider(),
                paymentEntity.getAmount(),
                paymentEntity.getCreatedAt()
        );
    }
}
