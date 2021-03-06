package com.codeborne.selenide.commands;

import com.codeborne.selenide.Command;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.InvalidStateException;
import com.codeborne.selenide.impl.WebElementSource;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.commands.Util.firstOf;
import static com.codeborne.selenide.impl.Events.events;

@ParametersAreNonnullByDefault
public class SetValue implements Command<SelenideElement> {
  @Override
  @Nonnull
  public SelenideElement execute(SelenideElement proxy, WebElementSource locator, @Nullable Object[] args) {
    String text = firstOf(args);
    WebElement element = locator.findAndAssertElementIsInteractable();
    Driver driver = locator.driver();

    setValueForTextInput(driver, element, text);
    return proxy;
  }

  private void setValueForTextInput(Driver driver, WebElement element, @Nullable String text) {
    if (text == null || text.isEmpty()) {
      element.clear();
    }
    else if (driver.config().fastSetValue()) {
      String error = setValueByJs(driver, element, text);
      if (error != null) throw new InvalidStateException(error);
      else {
        events.fireEvent(driver, element, "keydown", "keypress", "input", "keyup", "change");
      }
    }
    else {
      element.clear();
      element.sendKeys(text);
    }
  }

  private String setValueByJs(Driver driver, WebElement element, String text) {
    return driver.executeJavaScript(
        "return (function(webelement, text) {" +
            "if (webelement.getAttribute('readonly') != undefined) return 'Cannot change value of readonly element';" +
            "if (webelement.getAttribute('disabled') != undefined) return 'Cannot change value of disabled element';" +
            "webelement.focus();" +
            "var maxlength = webelement.getAttribute('maxlength') == null ? -1 : parseInt(webelement.getAttribute('maxlength'));" +
            "webelement.value = " +
            "maxlength == -1 ? text " +
            ": text.length <= maxlength ? text " +
            ": text.substring(0, maxlength);" +
            "return null;" +
            "})(arguments[0], arguments[1]);",
        element, text);
  }
}
