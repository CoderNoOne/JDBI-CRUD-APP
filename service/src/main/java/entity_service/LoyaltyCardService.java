package service.entity_service;

import lombok.RequiredArgsConstructor;
import model.entity.Customer;
import model.entity.LoyaltyCard;
import repository.entity_repository.impl.LoyaltyCardRepository;
import validators.impl.LoyaltyCardValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static utils.others.SimulateTimeFlowUtils.getClock;
import static utils.others.UserDataUtils.getString;
import static utils.others.UserDataUtils.printMessage;

@RequiredArgsConstructor
public class LoyaltyCardService {

  private final int LOYALTY_CARD_MIN_MOVIE_NUMBER = 3;
  private final LoyaltyCardRepository loyaltyCardRepository;

  private LoyaltyCard createLoyaltyCard(BigDecimal discount, LocalDate expirationDate, Integer moviesNumber) {
    return LoyaltyCard.builder().discount(discount).expirationDate(expirationDate).moviesNumber(moviesNumber).build();
  }

  private boolean addLoyaltyCard(BigDecimal discount, LocalDate expirationDate, Integer moviesNumber) {
    var loyaltyCard = createLoyaltyCard(discount, expirationDate, moviesNumber);
    boolean isValid = new LoyaltyCardValidator().validateEntity(loyaltyCard);

    if (isValid) {
      loyaltyCardRepository.add(loyaltyCard);
    }
    return isValid;

  }

  private void addLoyaltyCardForCustomer(Customer customer) {
    if (addNewLoyaltyCard()) {
      customer.setLoyaltyCardId(getNewlyCreatedLoyaltyCardId());
    }
  }

  private boolean addNewLoyaltyCard() {
    return addLoyaltyCard(new BigDecimal("5"), LocalDate.now(getClock()).plusMonths(3), 2);
  }

  private Integer getNewlyCreatedLoyaltyCardId() {
    return loyaltyCardRepository.findAll().get(loyaltyCardRepository.findAll().size() - 1).getId();
  }

  public void decreaseMoviesNumberByLoyaltyCardId(Integer loyaltyCardId) {
    var loyaltyCardOptional = loyaltyCardRepository.findById(loyaltyCardId);

    if (loyaltyCardOptional.isPresent() && loyaltyCardOptional.get().getExpirationDate().compareTo(LocalDate.now(getClock())) > 0) {
      var loyaltyCard = loyaltyCardOptional.get();
      loyaltyCard.setMoviesNumber(loyaltyCard.getMoviesNumber() - 1);
      loyaltyCardRepository.update(loyaltyCard);
    }
  }

  public void askForLoyaltyCard(Customer customer) {
    if (getString("Do you want to add a loyalty card? (y/n)").toUpperCase().equalsIgnoreCase("y")) {
      addLoyaltyCardForCustomer(customer);
      printMessage("Loyalty card successfully added to you account!");
    } else {
      printMessage("Too bad. Maybe next time");
    }
  }

  public Optional<LoyaltyCard> findLoyaltyCardById(Integer id) {
    return loyaltyCardRepository.findById(id);
  }

  public int getLoyaltyMinMovieCard() {
    return LOYALTY_CARD_MIN_MOVIE_NUMBER;
  }
}