package bank.services;

import bank.entity.Client;
import bank.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService
{
    @Autowired
    ClientRepository clientRepository;

    public List<Client> getAll(){
        return clientRepository.findAll();
    }

    public Client getOne(UUID id){
        return clientRepository.getOne(id);
    }

    public void save(Client client){
        clientRepository.save(client);
    }
}

