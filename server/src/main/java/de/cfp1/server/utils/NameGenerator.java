package de.cfp1.server.utils;

import java.util.Random;

/**
 * @author robert.kratz
 */

public class NameGenerator {

  private static final String[] adjectives = {
      "Quick", "Lazy", "Charming", "Clever", "Witty",
      "Brave", "Mighty", "Sneaky", "Friendly", "Loyal",
      "Happy", "Gloomy", "Radiant", "Gentle", "Fierce",
      "Ancient", "Modern", "Mystic", "Cosmic", "Epic",
      "Noble", "Wild", "Tranquil", "Alert", "Bright", "Grated", "Sponge", "Stinky"
  };

  private static final String[] nouns = {
      "Wizard", "Dragon", "Knight", "Castle", "Shadow",
      "Phantom", "Voyager", "Pirate", "Ninja", "Samurai",
      "Robot", "Giant", "Elf", "Dwarf", "Tiger",
      "Eagle", "Lion", "Panther", "Wizard", "Goblin",
      "Bear", "Wolf", "Fox", "Raven", "Sphinx", "Rob", "Benji", "Gil", "Joel", "Juan", "Chirs"
  };

  private static final Random random = new Random();

  /**
   * Generates a random username
   *
   * @return A random username
   * @author robert.kratz
   */
  public static String generateUsername() {
    String adjective = adjectives[random.nextInt(adjectives.length)];
    String noun = nouns[random.nextInt(nouns.length)];
    String username = adjective + noun;

    if (random.nextBoolean()) {
      int number = random.nextInt(90) + 10; // generates a number between 10 and 99
      username += number;
    }

    return username;
  }
}
