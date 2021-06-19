package bank.services;

import bank.entity.Bank;
import bank.entity.Client;
import bank.entity.Credit;
import bank.repositories.BankRepository;
import bank.repositories.ClientRepository;
import org.hibernate.type.UUIDCharType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ClientService
{
    @Autowired
    ClientRepository clientRepository;



    public void delete(Client client){
        clientRepository.delete(client);
    }

    public  void deleteAll(List<Client> clients){
        clientRepository.deleteAll(clients);
    }

    public List<Client> getAll(){
        return clientRepository.findAll();
    }
    @Transactional
    public Client getOne(UUID id){
        return clientRepository.getOne(id);
    }

    public void save(Client client){
        clientRepository.save(client);
    }
    @Transactional
    public void removeBank(UUID clientId,Bank bank){
            Client client = getOne(clientId);
            client.getClientBanks().remove(bank);
    }

    @Transactional
    public void removeAllBank(UUID clientId){
        Client client = getOne(clientId);
        client.getClientBanks().removeAll(client.getClientBanks());
        save(client);
    }


//    public void addBanks(UUID clientId, UUID bankId){
//        Client client = getOne(clientId);
//        Bank bank = bankService.getOne(bankId);
//        Set<Bank> banks = client.getClientBanks();
//        banks.add(bank);
//        Set<Client> clients = bank.getClients();
//        clients.add(client);
//        update(client);
//        bankService.save(bank);
//    }

}

