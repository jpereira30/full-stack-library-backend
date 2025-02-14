package com.jpereira30.library_api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class OpenAIResponseTest {

  @Test
  void testSetAndGetChoices() {
    // Arrange
    Choice choice1 = new Choice();
    choice1.setText("Choice 1");

    Choice choice2 = new Choice();
    choice2.setText("Choice 2");

    OpenAIResponse response = new OpenAIResponse();

    // Act
    response.setChoices(List.of(choice1, choice2));

    // Assert
    assertThat(response.getChoices())
        .hasSize(2)
        .extracting(Choice::getText)
        .containsExactly("Choice 1", "Choice 2");
  }

  @Test
  void testDefaultConstructor() {
    // Act
    OpenAIResponse response = new OpenAIResponse();

    // Assert
    assertThat(response).isNotNull();
  }

  @Test
  void testEqualsAndHashCode() {
    // Arrange
    Choice choice = new Choice();
    choice.setText("Test choice");

    OpenAIResponse response1 = new OpenAIResponse();
    response1.setChoices(List.of(choice));

    OpenAIResponse response2 = new OpenAIResponse();
    response2.setChoices(List.of(choice));

    // Assert
    assertThat(response1).isEqualTo(response2);
    assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
  }

  @Test
  void testToString() {
    // Arrange
    Choice choice = new Choice();
    choice.setText("Test choice");

    OpenAIResponse response = new OpenAIResponse();
    response.setChoices(List.of(choice));

    // Act
    String result = response.toString();

    // Assert
    assertThat(result).contains("choices=[");
  }
}
