package com.example.waud.loginslider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.example.waud.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class FirstFragmentDirections {
  private FirstFragmentDirections() {
  }

  @NonNull
  public static ActionFirstFragmentToSecondFragment actionFirstFragmentToSecondFragment(
      @NonNull String photoUrl, @NonNull String displayName, @NonNull String UID) {
    return new ActionFirstFragmentToSecondFragment(photoUrl, displayName, UID);
  }

  public static class ActionFirstFragmentToSecondFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionFirstFragmentToSecondFragment(@NonNull String photoUrl,
        @NonNull String displayName, @NonNull String UID) {
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
    public ActionFirstFragmentToSecondFragment setPhotoUrl(@NonNull String photoUrl) {
      if (photoUrl == null) {
        throw new IllegalArgumentException("Argument \"photoUrl\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("photoUrl", photoUrl);
      return this;
    }

    @NonNull
    public ActionFirstFragmentToSecondFragment setDisplayName(@NonNull String displayName) {
      if (displayName == null) {
        throw new IllegalArgumentException("Argument \"displayName\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("displayName", displayName);
      return this;
    }

    @NonNull
    public ActionFirstFragmentToSecondFragment setUID(@NonNull String UID) {
      if (UID == null) {
        throw new IllegalArgumentException("Argument \"UID\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("UID", UID);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
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
    public int getActionId() {
      return R.id.action_firstFragment_to_secondFragment;
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

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionFirstFragmentToSecondFragment that = (ActionFirstFragmentToSecondFragment) object;
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
      if (getActionId() != that.getActionId()) {
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
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionFirstFragmentToSecondFragment(actionId=" + getActionId() + "){"
          + "photoUrl=" + getPhotoUrl()
          + ", displayName=" + getDisplayName()
          + ", UID=" + getUID()
          + "}";
    }
  }
}
