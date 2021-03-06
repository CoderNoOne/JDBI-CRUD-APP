package entity_service;

import entity.Movie;
import entity_repository.impl.MovieRepository;
import exceptions.AppException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@DisplayName("Test cases for movie service")
@Tag("Services")
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {


  @Mock
  private MovieRepository movieRepository;

  @InjectMocks
  private MovieService movieService;

  @Test
  @DisplayName("Get all movies")
  void test1() {


    //given
    List<Movie> expectedResult = List.of(
            Movie.builder()
                    .id(1)
                    .title("TITLE")
                    .duration(2)
                    .genre("GENRE")
                    .releaseDate(LocalDate.of(2020, 10, 20))
                    .price(new BigDecimal("30"))
                    .build()
    );


    given(movieRepository.findAll())
            .willReturn(expectedResult);

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      List<Movie> actualResult = movieService.getAllMovies();
      assertThat(actualResult, is(equalTo(expectedResult)));
    });

    then(movieRepository).should(times(1)).findAll();
    then(movieRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("Get Movie by id : case id is null")
  void test2() {

    //given
    Integer id = null;
    String expectedExceptionMessage = "Movie id is null";

    //when
    //then
    AppException actualException = Assertions.assertThrows(AppException.class, () -> movieService.getMovieById(id));
    assertThat(actualException.getExceptionMessage(), is(equalTo(expectedExceptionMessage)));

    then(movieRepository).shouldHaveZeroInteractions();
  }

  @Test
  @DisplayName("Get movie by id : case id is not null")
  void test3() {

    //given
    Integer id = 2;

    Optional<Movie> expectedResult = Optional.of(Movie.builder()
            .id(id)
            .title("TITLE")
            .genre("GENRE")
            .price(new BigDecimal("35"))
            .duration(2)
            .releaseDate(LocalDate.of(2020, Month.OCTOBER, 20))
            .build());


    given(movieRepository.findById(id))
            .willReturn(expectedResult);

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      Optional<Movie> actualResult = movieService.getMovieById(id);
      assertThat(actualResult, is(equalTo(expectedResult)));
    });

    then(movieRepository).should(times(1)).findById(id);
    then(movieRepository).shouldHaveNoMoreInteractions();

  }

  @Test
  @DisplayName("Delete movie by id : case id is null")
  void test4() {

    //given
    Integer id = null;
    String expectedExceptionMessage = "Movie id is null";

    //when
    //then
    AppException actualException = Assertions.assertThrows(AppException.class, () -> movieService.deleteMovie(id));
    assertThat(actualException.getExceptionMessage(), is(equalTo(expectedExceptionMessage)));

    then(movieRepository).shouldHaveZeroInteractions();
  }

  @Test
  @DisplayName("Delete movie by id : case id is not null")
  void test5() {

    //given
    Integer id = 6;

    //when
    //then
    Assertions.assertDoesNotThrow(() -> movieService.deleteMovie(id));

    then(movieRepository).should(times(1)).delete(id);
    then(movieRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("Add Movie : case movie is null")
  void test6() {

    //given
    Movie movie = null;
    String expectedExceptionMessage = "Movie is null";

    //when
    //then
    AppException actualException = Assertions.assertThrows(AppException.class, () -> movieService.addMovie(movie));
    assertThat(actualException.getExceptionMessage(), is(equalTo(expectedExceptionMessage)));

    then(movieRepository).shouldHaveZeroInteractions();
  }

  @Test
  @DisplayName("Add movie : case movie is not null: movie is not valid")
  void test7() {

    //given
    Movie movie = Movie.builder()
            .id(1)
            .genre("genre")
            .title("title")
            .build();

    //then
    //when
    Assertions.assertDoesNotThrow(() -> {
      boolean actualResult = movieService.addMovie(movie);
      assertThat(actualResult, is(equalTo(false)));
    });

    then(movieRepository).shouldHaveZeroInteractions();
  }

  @Test
  @DisplayName("Add movie : case movie is not null and movie is valid")
  void test8() {


    //given
    Movie movie = Movie.builder()
            .id(1)
            .title("TITLE")
            .duration(2)
            .releaseDate(LocalDate.now().plusMonths(2L))
            .price(new BigDecimal("30"))
            .genre("GENRE")
            .build();

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      boolean actualResult = movieService.addMovie(movie);
      assertThat(actualResult, is(equalTo(true)));
    });

    then(movieRepository).should(times(1)).add(movie);
    then(movieRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("Update movie : case movie is null")
  void test9() {

    //given
    Movie movie = null;
    String expectedExceptionMessage = "Movie is null";

    //when
    //then
    AppException actualException = Assertions.assertThrows(AppException.class, () -> movieService.updateMovie(movie));
    assertThat(actualException.getExceptionMessage(), is(equalTo(expectedExceptionMessage)));


    then(movieRepository).shouldHaveZeroInteractions();
  }


  @Test
  @DisplayName("Update movie : case movie is not null: movie is not valid")
  void test10() {

    //given
    Movie movie = Movie.builder()
            .genre("genre")
            .build();

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      boolean actualResult = movieService.updateMovie(movie);
      assertThat(actualResult, is(equalTo(false)));
    });

    then(movieRepository).shouldHaveZeroInteractions();

  }

  @Test
  @DisplayName("Update movie : case movie is not null : movie is valid")
  void test11() {

    //given
    Movie movie = Movie.builder()
            .genre("NEW GENRE")
            .build();

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      boolean actualResult = movieService.updateMovie(movie);
      assertThat(actualResult, is(equalTo(true)));
    });

    then(movieRepository).should(times(1)).update(movie);
    then(movieRepository).shouldHaveNoMoreInteractions();

  }

  @Test
  @DisplayName("get Average movie duration")
  void test12() {

    //given

    List<Movie> movieList = getMovieList();

    given(movieRepository.findAll())
            .willReturn(movieList);

    Map<String, Double> expectedResult = Map.of(
            "GENRE ONE", 3.0,
            "GENRE TWO", 1.0
    );

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      Map<String, Double> actualResult = movieService.getAverageMovieDurationForMovieGenre();
      assertThat(actualResult, is(equalTo(expectedResult)));
    });

    then(movieRepository).should(times(1)).findAll();
    then(movieRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("get most expensive movie for each genre")
  void test13() {

    //given
    List<Movie> movieList = getMovieList();

    Map<String, List<Movie>> expectedResult = Map.of(
            "GENRE ONE", List.of(Movie.builder()
                            .id(1)
                            .duration(2)
                            .genre("GENRE ONE")
                            .price(new BigDecimal("35"))
                            .releaseDate(LocalDate.of(2020, Month.OCTOBER, 20))
                            .title("TITLE ONE")
                            .build(),

                    Movie.builder()
                            .id(2)
                            .duration(4)
                            .genre("GENRE ONE")
                            .price(new BigDecimal("35"))
                            .releaseDate(LocalDate.of(2022, Month.MARCH, 15))
                            .title("TITLE TWO")
                            .build()),
            "GENRE TWO", Collections.singletonList(Movie.builder()
                    .id(3)
                    .duration(1)
                    .genre("GENRE TWO")
                    .price(new BigDecimal("40"))
                    .releaseDate(LocalDate.of(2021, Month.APRIL, 21))
                    .title("TITLE THREE")
                    .build())

    );

    given(movieRepository.findAll())
            .willReturn(movieList);
    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      Map<String, List<Movie>> actualResult = movieService.getMostExpensiveMovieForEachGenre();
      assertThat(actualResult, is(equalTo(expectedResult)));
    });

    then(movieRepository).should(times(1)).findAll();
    then(movieRepository).shouldHaveNoMoreInteractions();

  }

  @Test
  @DisplayName("get cheapest movie for each genre")
  void test14() {

    //given
    List<Movie> movieList = getMovieList();
    Map<String, List<Movie>> expectedResult = Map.of(
            "GENRE ONE", List.of(Movie.builder()
                            .id(1)
                            .duration(2)
                            .genre("GENRE ONE")
                            .price(new BigDecimal("35"))
                            .releaseDate(LocalDate.of(2020, Month.OCTOBER, 20))
                            .title("TITLE ONE")
                            .build(),

                    Movie.builder()
                            .id(2)
                            .duration(4)
                            .genre("GENRE ONE")
                            .price(new BigDecimal("35"))
                            .releaseDate(LocalDate.of(2022, Month.MARCH, 15))
                            .title("TITLE TWO")
                            .build()),
            "GENRE TWO", Collections.singletonList(Movie.builder()
                    .id(3)
                    .duration(1)
                    .genre("GENRE TWO")
                    .price(new BigDecimal("40"))
                    .releaseDate(LocalDate.of(2021, Month.APRIL, 21))
                    .title("TITLE THREE")
                    .build())

    );

    given(movieRepository.findAll()).willReturn(movieList);

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      Map<String, List<Movie>> actualResult = movieService.getCheapestMovieForEachGenre();
      assertThat(actualResult, is(equalTo(expectedResult)));
    });

    then(movieRepository).should(times(1)).findAll();
    then(movieRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("get the earliest premiere for movie genre")
  void test15() {

    //given

    given(movieRepository.findAll()).willReturn(getMovieList());

    Map<String, Map<LocalDate, List<Movie>>> expectedResult = Map.of(
            "GENRE ONE", Map.of(
                    LocalDate.of(2020, Month.OCTOBER, 20),
                    Collections.singletonList(Movie.builder()
                            .id(1)
                            .duration(2)
                            .genre("GENRE ONE")
                            .price(new BigDecimal("35"))
                            .releaseDate(LocalDate.of(2020, Month.OCTOBER, 20))
                            .title("TITLE ONE")
                            .build())),

                    "GENRE TWO", Map.of(
                            LocalDate.of(2021, Month.APRIL, 21),
                            Collections.singletonList(Movie.builder()
                                    .id(3)
                                    .duration(1)
                                    .genre("GENRE TWO")
                                    .price(new BigDecimal("40"))
                                    .releaseDate(LocalDate.of(2021, Month.APRIL, 21))
                                    .title("TITLE THREE")
                                    .build())
                    ));

    //when
    //then
    Assertions.assertDoesNotThrow(() -> {
      Map<String, Map<LocalDate, List<Movie>>> actualResult = movieService.getTheEarliestPremiereForMovieGenre();
      assertThat(actualResult, is(equalTo(expectedResult)));
    });

    then(movieRepository).should(times(1)).findAll();
    then(movieRepository).shouldHaveNoMoreInteractions();
  }


  private List<Movie> getMovieList() {

    return List.of(
            Movie.builder()
                    .id(1)
                    .duration(2)
                    .genre("GENRE ONE")
                    .price(new BigDecimal("35"))
                    .releaseDate(LocalDate.of(2020, Month.OCTOBER, 20))
                    .title("TITLE ONE")
                    .build(),

            Movie.builder()
                    .id(2)
                    .duration(4)
                    .genre("GENRE ONE")
                    .price(new BigDecimal("35"))
                    .releaseDate(LocalDate.of(2022, Month.MARCH, 15))
                    .title("TITLE TWO")
                    .build(),

            Movie.builder()
                    .id(3)
                    .duration(1)
                    .genre("GENRE TWO")
                    .price(new BigDecimal("40"))
                    .releaseDate(LocalDate.of(2021, Month.APRIL, 21))
                    .title("TITLE THREE")
                    .build());
  }
}
