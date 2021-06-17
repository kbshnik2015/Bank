package bank.services;

import bank.entity.Bank;
import bank.entity.Client;
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

    public  void deleteAll(List<Bank> clients){
        bankRepository.deleteAll(clients);
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

    public void update(Bank client){
        if(client.getId()!=null && bankRepository.getOne(client.getId()).getId().equals(client.getId())){
            bankRepository.delete(bankRepository.getOne(client.getId()));
            bankRepository.save(client);
        }
    }

    public void removeClientFromBank(Client client, UUID bankId){
        Bank bank = getOne(bankId);
        bank.getClients().remove(client);
        bankRepository.save(bank);
    }
}
