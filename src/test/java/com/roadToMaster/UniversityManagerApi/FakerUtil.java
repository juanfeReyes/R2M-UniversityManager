package com.roadToMaster.UniversityManagerApi;

import com.github.javafaker.Faker;

import java.util.Random;

public class FakerUtil {

  public static Faker buildFaker() {
    return new Faker(new Random(1));
  }
}
