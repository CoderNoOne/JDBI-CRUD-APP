package service.others;

import converters.impl.CustomerListJsonConverter;
import converters.impl.MovieListJsonConverter;
import exceptions.AppException;
import repository.entity_repository.impl.CustomerRepository;
import repository.entity_repository.impl.LoyaltyCardRepository;
import repository.entity_repository.impl.MovieRepository;
import repository.entity_repository.impl.SalesStandRepository;
import utils.others.UserDataUtils;
import validators.impl.CustomerValidator;
import validators.impl.MovieValidator;


import java.util.concurrent.atomic.AtomicInteger;

import static utils.others.UserDataUtils.printMessage;

public class DataInitializeService {

  private final MovieRepository MOVIE_REPOSITORY = new MovieRepository();
  private final CustomerRepository CUSTOMER_REPOSITORY = new CustomerRepository();
  private final LoyaltyCardRepository LOYALTY_CARD_REPOSITORY = new LoyaltyCardRepository();
  private final SalesStandRepository SALES_STAND_REPOSITORY = new SalesStandRepository();

  private DataInitializeService() {
  }

  public  void init() {
    deleteSalesStands();
    deleteCustomers();
    deleteLoyaltyCards();
    deleteMovies();
    initMovies("exampleMovies.json");
    initCustomers("exampleCustomers.json");
  }

  private  void initMovies(final String moviesJsonFilename) {

    var movieValidator = new MovieValidator();
    AtomicInteger atomicInteger = new AtomicInteger(1);
    MOVIE_REPOSITORY.deleteAll();
    new MovieListJsonConverter(moviesJsonFilename)
            .fromJson()
            .orElseThrow(() -> new AppException("FILE " + moviesJsonFilename + " is empty"))
            .stream()
            .filter(movie -> {

              if (movieValidator.hasErrors()) {
                printMessage("MOVIE NO: " + atomicInteger.get());
                movieValidator.validateEntity(movie);
              }
              atomicInteger.incrementAndGet();
              return !movieValidator.hasErrors();
            }).forEach(MOVIE_REPOSITORY::add);
  }

  private static void initCustomers(final String customersJsonFilenam) {
    CUSTOMER_REPOSITORY.deleteAll();

    var customerValidator = new CustomerValidator();
    AtomicInteger atomicInteger = new AtomicInteger(1);
    CUSTOMER_REPOSITORY.deleteAll();
    new CustomerListJsonConverter(customersJsonFilenam)
            .fromJson()
            .orElseThrow(() -> new AppException("FILE " + customersJsonFilenam + " is empty"))
            .stream()
            .filter(customer -> {
              if (customerValidator.hasErrors()) {
                printMessage("CUSTOMER NO: " + atomicInteger.get());
                customerValidator.validateEntity(customer);
              }
              atomicInteger.incrementAndGet();
              return !customerValidator.hasErrors();
            }).forEach(CUSTOMER_REPOSITORY::add);
  }


  private static void deleteLoyaltyCards() {
    LOYALTY_CARD_REPOSITORY.deleteAll();
  }

  private static void deleteSalesStands() {
    SALES_STAND_REPOSITORY.deleteAll();
  }

  private static void deleteCustomers() {
    CUSTOMER_REPOSITORY.deleteAll();
  }

  private static void deleteMovies() {
    MOVIE_REPOSITORY.deleteAll();
  }
}
