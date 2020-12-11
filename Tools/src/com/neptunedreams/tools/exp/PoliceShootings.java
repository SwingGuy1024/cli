package com.neptunedreams.tools.exp;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 10/27/20
 * <p>Time: 10:28 AM
 *
 * @author Miguel Mu\u00f1oz
 */
public class PoliceShootings {
  //id, name, date, manner_of_death, armed, age, gender, race, city, state, signs_of_mental_illness, threat_level, flee, body_camera, longitude, latitude, is_geocoding_exact

  public static class Incident {
    int id;
    String name;
    LocalDate date;
    String manner_of_death;
    String armed;
    int age;
    char gender;
    char race;
    String city;
    String state;
    boolean signs_of_mental_illness;
    String threat_level;
    String flee;
    boolean body_camera;
    BigDecimal longitude;
    BigDecimal latitude;
    boolean is_geocoding_exact;
    
    

  }
}
