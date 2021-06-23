package bank.services;

import bank.entity.OneTimePayment;
import bank.repositories.OneTimePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OneTimePaymentService
{
    @Autowired
    OneTimePaymentRepository oneTimePaymentRepository;

    public void delete(OneTimePayment oneTimePayment){
        oneTimePaymentRepository.delete(oneTimePayment);
    }

    public  void deleteAll(List<OneTimePayment> oneTimePaymentsts){
        oneTimePaymentRepository.deleteAll(oneTimePaymentsts);
    }

    public List<OneTimePayment> getAll(){
        return oneTimePaymentRepository.findAll();
    }

    public OneTimePayment getOne(UUID id){
        return oneTimePaymentRepository.getOne(id);
    }

    public void save(OneTimePayment client){
        oneTimePaymentRepository.save(client);
    }
}
