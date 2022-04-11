package com.roadToMaster.UniversityManagerApi;

import com.github.javafaker.Faker;

import java.util.Random;

public class FakerUtil {
  private static final Random seed = new Random(1);

  public static Faker buildFaker() {
    return new Faker(seed);
  }
}
