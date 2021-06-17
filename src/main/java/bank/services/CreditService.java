package bank.services;

import bank.entity.Credit;
import bank.repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditService 
{
    @Autowired
    CreditRepository creditRepository;

    public void delete(Credit credit){
        creditRepository.delete(credit);
    }

    public  void deleteAll(List<Credit> credits){
        creditRepository.deleteAll(credits);
    }

    public List<Credit> getAll(){
        return creditRepository.findAll();
    }

    public Credit getOne(UUID id){
        return creditRepository.getOne(id);
    }

    public void save(Credit credit){
        creditRepository.save(credit);
    }

    public void update(Credit credit){
        if(credit.getId()!=null && creditRepository.getOne(credit.getId()).getId().equals(credit.getId())){
            creditRepository.delete(creditRepository.getOne(credit.getId()));
            creditRepository.save(credit);
        }
    }
}
