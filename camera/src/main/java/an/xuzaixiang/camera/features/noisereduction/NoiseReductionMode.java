package an.xuzaixiang.camera.features.noisereduction;

public enum NoiseReductionMode {
  off("off"),
  fast("fast"),
  highQuality("highQuality"),
  minimal("minimal"),
  zeroShutterLag("zeroShutterLag");

  private final String strValue;

  NoiseReductionMode(String strValue) {
    this.strValue = strValue;
  }

  public static NoiseReductionMode getValueForString(String modeStr) {
    for (NoiseReductionMode value : values()) {
      if (value.strValue.equals(modeStr)) return value;
    }
    return null;
  }

  @Override
  public String toString() {
    return strValue;
  }
}
