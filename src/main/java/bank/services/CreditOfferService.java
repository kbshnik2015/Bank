package bank.services;

import bank.entity.CreditOffer;
import bank.repositories.CreditOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditOfferService
{
    @Autowired
    CreditOfferRepository creditOfferRepository;

    public void delete(CreditOffer creditOffers){
        creditOfferRepository.delete(creditOffers);
    }

    public  void deleteAll(List<CreditOffer> creditOffers){
        creditOfferRepository.deleteAll(creditOffers);
    }

    public List<CreditOffer> getAll(){
        return creditOfferRepository.findAll();
    }

    public CreditOffer getOne(UUID id){
        return creditOfferRepository.getOne(id);
    }

    public void save(CreditOffer creditOffers){
        creditOfferRepository.save(creditOffers);
    }
}
