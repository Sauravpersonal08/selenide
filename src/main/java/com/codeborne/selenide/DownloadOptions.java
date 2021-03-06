package com.codeborne.selenide;

import com.codeborne.selenide.files.DownloadAction;
import com.codeborne.selenide.files.FileFilter;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.files.DownloadActions.click;
import static com.codeborne.selenide.files.FileFilters.none;

@ParametersAreNonnullByDefault
public class DownloadOptions {
  private static final long UNSPECIFIED_TIMEOUT = Long.MIN_VALUE;

  private final FileDownloadMode method;
  private final long timeout;
  private final FileFilter filter;
  private final DownloadAction action;

  private DownloadOptions(FileDownloadMode method, long timeout, FileFilter filter, DownloadAction action) {
    this.method = method;
    this.timeout = timeout;
    this.filter = filter;
    this.action = action;
  }

  @CheckReturnValue
  @Nonnull
  public FileDownloadMode getMethod() {
    return method;
  }

  @CheckReturnValue
  public long getTimeout(long defaultValue) {
    return hasSpecifiedTimed() ? timeout : defaultValue;
  }

  private boolean hasSpecifiedTimed() {
    return timeout != UNSPECIFIED_TIMEOUT;
  }

  @CheckReturnValue
  @Nonnull
  public FileFilter getFilter() {
    return filter;
  }

  @CheckReturnValue
  @Nonnull
  public DownloadAction getAction() {
    return action;
  }

  @CheckReturnValue
  @Nonnull
  public DownloadOptions withTimeout(long timeout) {
    return new DownloadOptions(method, timeout, filter, action);
  }

  @CheckReturnValue
  @Nonnull
  public DownloadOptions withFilter(FileFilter filter) {
    return new DownloadOptions(method, timeout, filter, action);
  }

  /**
   * An user action to start the downloading process.
   * By default it's a click.
   * <p>
   * Use this method if you need to close some alert before downloading file etc.
   *
   * @param action any lambda accepting a Driver and WebElement (the element being clicked).
   * @return DownloadOptions
   * @since 5.22.0
   */
  public DownloadOptions withAction(DownloadAction action) {
    return new DownloadOptions(method, timeout, filter, action);
  }

  @Override
  public String toString() {
    if (hasSpecifiedTimed() && !filter.isEmpty())
      return String.format("method: %s, timeout: %s ms, filter: %s", method, timeout, filter.description());
    else if (hasSpecifiedTimed())
      return String.format("method: %s, timeout: %s ms", method, timeout);
    else if (!filter.isEmpty())
      return String.format("method: %s, filter: %s", method, filter.description());
    else
      return String.format("method: %s", method);
  }

  @CheckReturnValue
  @Nonnull
  public static DownloadOptions using(FileDownloadMode method) {
    return new DownloadOptions(method, UNSPECIFIED_TIMEOUT, none(), click());
  }
}
