package com.example.waud.loginslider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class SecondFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private SecondFragmentArgs() {
  }

  private SecondFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static SecondFragmentArgs fromBundle(@NonNull Bundle bundle) {
    SecondFragmentArgs __result = new SecondFragmentArgs();
    bundle.setClassLoader(SecondFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("photoUrl")) {
      String photoUrl;
      photoUrl = bundle.getString("photoUrl");
      if (photoUrl == null) {
        throw new IllegalArgumentException("Argument \"photoUrl\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("photoUrl", photoUrl);
    } else {
      throw new IllegalArgumentException("Required argument \"photoUrl\" is missing and does not have an android:defaultValue");
    }
    if (bundle.containsKey("displayName")) {
      String displayName;
      displayName = bundle.getString("displayName");
      if (displayName == null) {
        throw new IllegalArgumentException("Argument \"displayName\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("displayName", displayName);
    } else {
      throw new IllegalArgumentException("Required argument \"displayName\" is missing and does not have an android:defaultValue");
    }
    if (bundle.containsKey("UID")) {
      String UID;
      UID = bundle.getString("UID");
      if (UID == null) {
        throw new IllegalArgumentException("Argument \"UID\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("UID", UID);
    } else {
      throw new IllegalArgumentException("Required argument \"UID\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getPhotoUrl() {
    return (String) arguments.get("photoUrl");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getDisplayName() {
    return (String) arguments.get("displayName");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getUID() {
    return (String) arguments.get("UID");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("photoUrl")) {
      String photoUrl = (String) arguments.get("photoUrl");
      __result.putString("photoUrl", photoUrl);
    }
    if (arguments.containsKey("displayName")) {
      String displayName = (String) arguments.get("displayName");
      __result.putString("displayName", displayName);
    }
    if (arguments.containsKey("UID")) {
      String UID = (String) arguments.get("UID");
      __result.putString("UID", UID);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    SecondFragmentArgs that = (SecondFragmentArgs) object;
    if (arguments.containsKey("photoUrl") != that.arguments.containsKey("photoUrl")) {
      return false;
    }
    if (getPhotoUrl() != null ? !getPhotoUrl().equals(that.getPhotoUrl()) : that.getPhotoUrl() != null) {
      return false;
    }
    if (arguments.containsKey("displayName") != that.arguments.containsKey("displayName")) {
      return false;
    }
    if (getDisplayName() != null ? !getDisplayName().equals(that.getDisplayName()) : that.getDisplayName() != null) {
      return false;
    }
    if (arguments.containsKey("UID") != that.arguments.containsKey("UID")) {
      return false;
    }
    if (getUID() != null ? !getUID().equals(that.getUID()) : that.getUID() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getPhotoUrl() != null ? getPhotoUrl().hashCode() : 0);
    result = 31 * result + (getDisplayName() != null ? getDisplayName().hashCode() : 0);
    result = 31 * result + (getUID() != null ? getUID().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SecondFragmentArgs{"
        + "photoUrl=" + getPhotoUrl()
        + ", displayName=" + getDisplayName()
        + ", UID=" + getUID()
        + "}";
  }

  public static class Builder {
    private final HashMap arguments = new HashMap();

    public Builder(SecondFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder(@NonNull String photoUrl, @NonNull String displayName, @NonNull String UID) {
      if (photoUrl == null) {
        throw new IllegalArgumentException("Argument \"photoUrl\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("photoUrl", photoUrl);
      if (displayName == null) {
        throw new IllegalArgumentException("Argument \"displayName\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("displayName", displayName);
      if (UID == null) {
        throw new IllegalArgumentException("Argument \"UID\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("UID", UID);
    }

    @NonNull
    public SecondFragmentArgs build() {
      SecondFragmentArgs result = new SecondFragmentArgs(arguments);
      return result;
    }

    @NonNull
    public Builder setPhotoUrl(@NonNull String photoUrl) {
      if (photoUrl == null) {
        throw new IllegalArgumentException("Argument \"photoUrl\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("photoUrl", photoUrl);
      return this;
    }

    @NonNull
    public Builder setDisplayName(@NonNull String displayName) {
      if (displayName == null) {
        throw new IllegalArgumentException("Argument \"displayName\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("displayName", displayName);
      return this;
    }

    @NonNull
    public Builder setUID(@NonNull String UID) {
      if (UID == null) {
        throw new IllegalArgumentException("Argument \"UID\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("UID", UID);
      return this;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getPhotoUrl() {
      return (String) arguments.get("photoUrl");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getDisplayName() {
      return (String) arguments.get("displayName");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getUID() {
      return (String) arguments.get("UID");
    }
  }
}
