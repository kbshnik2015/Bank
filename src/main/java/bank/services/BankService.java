package bank.services;

import bank.entity.Bank;
import bank.entity.Client;
import bank.entity.Credit;
import bank.repositories.BankRepository;
import bank.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BankService
{
    @Autowired
    BankRepository bankRepository;

    public void delete(Bank bank){
        bankRepository.delete(bank);
    }

    public  void deleteAll(List<Bank> banks){
        bankRepository.deleteAll(banks);
    }

    public List<Bank> getAll(){
        return bankRepository.findAll();
    }

    public Bank getOne(UUID id){
        return bankRepository.getOne(id);
    }

    public void save(Bank client){
        bankRepository.save(client);
    }


    public void addCredit(UUID bankId, Credit credit){
        Bank bank = getOne(bankId);
        bank.getCredits().add(credit);
        save(bank);
    }

    public void removeCredit(UUID bankId, Credit credit){
        Bank bank = getOne(bankId);
        bank.getCredits().remove(credit);
        save(bank);
    }

}
