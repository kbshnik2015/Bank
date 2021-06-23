package bank.repositories;

import bank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Transactional
public interface BankRepository extends JpaRepository<Bank, UUID>
{

}