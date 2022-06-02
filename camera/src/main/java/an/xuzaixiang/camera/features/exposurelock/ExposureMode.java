package an.xuzaixiang.camera.features.exposurelock;

public enum ExposureMode {
  auto("auto"),
  locked("locked");

  private final String strValue;

  ExposureMode(String strValue) {
    this.strValue = strValue;
  }

  public static ExposureMode getValueForString(String modeStr) {
    for (ExposureMode value : values()) {
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
