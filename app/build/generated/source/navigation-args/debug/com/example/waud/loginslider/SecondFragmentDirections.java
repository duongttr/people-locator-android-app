package com.example.waud.loginslider;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.waud.R;

public class SecondFragmentDirections {
  private SecondFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionSecondFragmentToFirstFragment() {
    return new ActionOnlyNavDirections(R.id.action_secondFragment_to_firstFragment);
  }
}
