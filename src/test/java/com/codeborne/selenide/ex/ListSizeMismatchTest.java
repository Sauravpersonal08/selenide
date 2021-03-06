package com.codeborne.selenide.ex;

import com.codeborne.selenide.impl.CollectionSource;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.Mocks.mockCollection;
import static com.codeborne.selenide.Mocks.mockElement;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

final class ListSizeMismatchTest {
  private final int expectedSize = 10;
  private final CollectionSource collection = mockCollection("Collection description");
  private final List<WebElement> actualElementsList = asList(mockElement("Niff"), mockElement("Naff"), mockElement("Nuff"));
  private final Exception exception = new Exception("Something happened");
  private final long timeoutMs = 1000L;

  @Test
  void toString_withoutExplanation() {
    ListSizeMismatch listSizeMismatch = new ListSizeMismatch("<=",
      expectedSize,
      null,
      collection,
      actualElementsList,
      exception,
      timeoutMs);

    assertThat(listSizeMismatch)
      .hasMessage(String.format("List size mismatch: expected: <= 10, actual: 3, collection: Collection description%n" +
        "Elements: [%n" +
        "\t<div displayed:false>Niff</div>,%n" +
        "\t<div displayed:false>Naff</div>,%n" +
        "\t<div displayed:false>Nuff</div>%n" +
        "]%n" +
        "Timeout: 1 s.%n" +
        "Caused by: java.lang.Exception: Something happened"));
  }

  @Test
  void toString_withExplanation() {
    ListSizeMismatch listSizeMismatch = new ListSizeMismatch(">",
      expectedSize,
      "it's said in customer requirement #12345",
      collection,
      actualElementsList,
      exception,
      timeoutMs);

    assertThat(listSizeMismatch)
      .hasMessage(String.format("List size mismatch: expected: > 10" +
        " (because it's said in customer requirement #12345), actual: 3, collection: Collection description%n" +
        "Elements: [%n" +
        "\t<div displayed:false>Niff</div>,%n" +
        "\t<div displayed:false>Naff</div>,%n" +
        "\t<div displayed:false>Nuff</div>%n" +
        "]%n" +
        "Timeout: 1 s.%n" +
        "Caused by: java.lang.Exception: Something happened"));
  }
}
