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

    public void delete(Client client){
        clientRepository.delete(client);
    }

    public  void deleteAll(List<Client> clients){
        clientRepository.deleteAll(clients);
    }

    public List<Client> getAll(){
        return clientRepository.findAll();
    }

    public Client getOne(UUID id){
        return clientRepository.getOne(id);
    }

    public void save(Client client){
        clientRepository.save(client);
    }

    public void update(Client client){
        if(client.getId()!=null && clientRepository.getOne(client.getId()).getId().equals(client.getId())){
            clientRepository.delete(clientRepository.getOne(client.getId()));
            clientRepository.save(client);
        }
        //TODO: выбросить эксепшн о том. что метод апдейт тут не подходит
    }
}

