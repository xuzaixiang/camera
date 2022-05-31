package an.xuzaixiang.camera.features.autofocus;

public enum FocusMode {
  auto("auto"),
  locked("locked");

  private final String strValue;

  FocusMode(String strValue) {
    this.strValue = strValue;
  }

  public static FocusMode getValueForString(String modeStr) {
    for (FocusMode value : values()) {
      if (value.strValue.equals(modeStr)) {
        return value;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return strValue;
  }
}
