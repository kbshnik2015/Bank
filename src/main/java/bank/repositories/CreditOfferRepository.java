package bank.repositories;

import bank.entity.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditOfferRepository extends JpaRepository<CreditOffer, UUID>
{

}