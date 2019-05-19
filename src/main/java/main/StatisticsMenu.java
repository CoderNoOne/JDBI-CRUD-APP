package main;

import exceptions.AppException;
import lombok.extern.slf4j.Slf4j;
import repository.entity_repository.impl.CustomerRepository;
import repository.entity_repository.impl.LoyaltyCardRepository;
import repository.entity_repository.impl.MovieRepository;
import repository.entity_repository.impl.SalesStandRepository;
import repository.others.JoinedEntitiesRepository;
import service.entity_service.CustomerService;
import service.entity_service.LoyaltyCardService;
import service.entity_service.MovieService;
import service.entity_service.SalesStandService;
import service.others.JoinedEntitiesService;
import utils.others.UserDataUtils;

import java.text.MessageFormat;
import java.util.Arrays;

import static utils.others.UserDataUtils.printMessage;

@Slf4j
class StatisticsMenu {

  private final CustomerService customerService = new CustomerService(new CustomerRepository());
  private final MovieService movieService = new MovieService(new MovieRepository());
  private final LoyaltyCardService loyaltyCardService = new LoyaltyCardService(new LoyaltyCardRepository());
  private final SalesStandService salesStandService = new SalesStandService(new SalesStandRepository());
  private final JoinedEntitiesService joinedEntitiesService = new JoinedEntitiesService(new JoinedEntitiesRepository());

  void menu() {

    menuOptions();
    while (true) {
      try {
        int option = UserDataUtils.getInt("INPUT YOUR OPTION: ");
        switch (option) {
          case 1 -> option7_1();
          case 2 -> option7_2();
          case 3 -> option7_3();
          case 4 -> option7_4();
          case 5 -> option7_5();
          case 6 -> option7_6();
          case 7 -> option7_7();
          case 8 -> menuOptions();
          case 9 -> new MainMenu().mainMenu();
          default -> throw new AppException("INPUT OPTION IS NOT DEFINED");
        }
      } catch (AppException e) {
        log.info(e.getExceptionMessage());
        log.error(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  public static void menuOptions() {

    printMessage(MessageFormat.format(
            "\nOption no. 1 - {0}\n" +
                    "Option no. 2 - {1}\n" +
                    "Option no. 3 - {2}\n" +
                    "Option no. 4 - {3}\n" +
                    "Option no. 5 - {4}\n" +
                    "Option no. 6 - {5}\n" +
                    "Option no. 7 - {6}\n" +
                    "Option no. 8 - {7}\n" +
                    "Option no. 9 - {8}",

            "Movies grouped by the most popular ones",
            "The most popular movie genre grouped by each customer ",
            "The most expensive ticket bought by each customer",
            "The cheapest ticket bought by each customer",
            "Average ticket price grouped by month",
            "Total monthly expenses on tickets grouped by month for each customer",
            "Total amount of tickets bought with discount grouped by movie category and for each customer",
            "Total amount of tickets bought without discount by grouped by movie category and for each customer",
            "Back to main menu"
    ));
  }

  //filmy pogrupowane wg najchętnie kupowanych
  private void option7_1() {
    joinedEntitiesService.movieGroupedByPopularity().forEach((movie, number) -> System.out.println("Movie: " + movie.getTitle()
            + " - > " + number));
  }

  private void option7_2() {
    joinedEntitiesService.mostPopularMovieGenreForEachCustomer().forEach((customerId, innerMap) -> {
      printMessage(customerService.findCustomerById(customerId).get().toString() + "\n");
      innerMap.forEach((category, number) -> printMessage("Category: " + category + " -> Bought:" + number + " times."));
      printMessage("\n");
    });
  }

  private void option7_3() {

  }

  private void option7_4() {

  }

  private void option7_5() {

  }

  private void option7_6() {

  }

  private void option7_7() {

  }

}
