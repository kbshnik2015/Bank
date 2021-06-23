package bank.repositories;

import bank.entity.OneTimePayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OneTimePaymentRepository extends JpaRepository<OneTimePayment, UUID>
{
}