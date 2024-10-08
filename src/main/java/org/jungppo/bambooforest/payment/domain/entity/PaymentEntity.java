package org.jungppo.bambooforest.payment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jungppo.bambooforest.battery.domain.BatteryItem;
import org.jungppo.bambooforest.global.jpa.domain.entity.JpaBaseEntity;
import org.jungppo.bambooforest.member.domain.entity.MemberEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment",
        indexes = {
                @Index(name = "idx_member_id_status_created_at", columnList = "member_id, status, created_at")
        })
public class PaymentEntity extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatusType status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatteryItem batteryItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "`key`", unique = true)
    private String key;

    private String provider;

    private BigDecimal amount; // 배터리 금액과 결제 금액이 다를 수 있음.

    private PaymentEntity(final PaymentStatusType status, final BatteryItem batteryItem, final MemberEntity member) {
        this.status = status;
        this.batteryItem = batteryItem;
        this.member = member;
    }

    public static PaymentEntity of(final PaymentStatusType status, final BatteryItem batteryItem,
                                   final MemberEntity member) {
        return new PaymentEntity(status, batteryItem, member);
    }

    public void updatePaymentStatus(final PaymentStatusType status) {
        this.status = status;
    }

    public void updatePaymentDetails(final String key, final String provider, final BigDecimal amount) {
        this.key = key;
        this.provider = provider;
        this.amount = amount;
    }
}
