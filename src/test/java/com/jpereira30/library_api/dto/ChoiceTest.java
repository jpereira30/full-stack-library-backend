package com.jpereira30.library_api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ChoiceTest {

  @Test
  void testSetAndGetText() {
    // Arrange
    Choice choice = new Choice();

    // Act
    choice.setText("Sample text");

    // Assert
    assertThat(choice.getText()).isEqualTo("Sample text");
  }

  @Test
  void testDefaultConstructor() {
    // Arrange & Act
    Choice choice = new Choice();

    // Assert
    assertThat(choice).isNotNull();
  }

  @Test
  void testEqualsAndHashCode() {
    // Arrange
    Choice choice1 = new Choice();
    choice1.setText("AI Insight");

    Choice choice2 = new Choice();
    choice2.setText("AI Insight");

    // Assert
    assertThat(choice1).isEqualTo(choice2);
    assertThat(choice1.hashCode()).isEqualTo(choice2.hashCode());
  }

  @Test
  void testToString() {
    // Arrange
    Choice choice = new Choice();
    choice.setText("Test string");

    // Act
    String result = choice.toString();

    // Assert
    assertThat(result).contains("text=Test string");
  }
}
