package utils.others;

import java.time.*;

public class SimulateTimeFlowUtils {

  private static Clock clock;
  private static int days;

  private SimulateTimeFlowUtils() {
  }

  public static void moveDateTimeForwardByDaysNumber(int days) {
    SimulateTimeFlowUtils.days += days;
    clock = Clock.offset(Clock.fixed(Instant.now(), ZoneId.systemDefault()), Duration.ofDays(days));
  }

  public static void moveDateTimeBackwardByDaysNumber(int days) {
    SimulateTimeFlowUtils.days -= days;
    clock = Clock.offset(Clock.fixed(Instant.now(), ZoneId.systemDefault()), Duration.ofDays(days));
  }

  public static Clock getClock() {
    clock = Clock.offset(Clock.fixed(Instant.now(), ZoneId.systemDefault()), Duration.ofDays(days));
    return clock;

  }
}