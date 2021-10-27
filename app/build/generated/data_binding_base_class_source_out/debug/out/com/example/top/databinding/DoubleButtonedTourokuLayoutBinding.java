// Generated by view binder compiler. Do not edit!
package com.example.top.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.top.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DoubleButtonedTourokuLayoutBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ListView doubleButtonedTourokuBlockList;

  @NonNull
  public final ListView doubleButtonedTourokuRoomList;

  @NonNull
  public final ListView doubleButtonedTourokuRyoseiList;

  @NonNull
  public final TextView doubleButtonedTourokuTitile;

  @NonNull
  public final TextView doubleJimutouNameShow;

  @NonNull
  public final Button doubleTourokuGoBackButton;

  private DoubleButtonedTourokuLayoutBinding(@NonNull ConstraintLayout rootView,
      @NonNull ListView doubleButtonedTourokuBlockList,
      @NonNull ListView doubleButtonedTourokuRoomList,
      @NonNull ListView doubleButtonedTourokuRyoseiList,
      @NonNull TextView doubleButtonedTourokuTitile, @NonNull TextView doubleJimutouNameShow,
      @NonNull Button doubleTourokuGoBackButton) {
    this.rootView = rootView;
    this.doubleButtonedTourokuBlockList = doubleButtonedTourokuBlockList;
    this.doubleButtonedTourokuRoomList = doubleButtonedTourokuRoomList;
    this.doubleButtonedTourokuRyoseiList = doubleButtonedTourokuRyoseiList;
    this.doubleButtonedTourokuTitile = doubleButtonedTourokuTitile;
    this.doubleJimutouNameShow = doubleJimutouNameShow;
    this.doubleTourokuGoBackButton = doubleTourokuGoBackButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DoubleButtonedTourokuLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DoubleButtonedTourokuLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.double_buttoned_touroku_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DoubleButtonedTourokuLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.double_buttoned_touroku_block_list;
      ListView doubleButtonedTourokuBlockList = ViewBindings.findChildViewById(rootView, id);
      if (doubleButtonedTourokuBlockList == null) {
        break missingId;
      }

      id = R.id.double_buttoned_touroku_room_list;
      ListView doubleButtonedTourokuRoomList = ViewBindings.findChildViewById(rootView, id);
      if (doubleButtonedTourokuRoomList == null) {
        break missingId;
      }

      id = R.id.double_buttoned_touroku_ryosei_list;
      ListView doubleButtonedTourokuRyoseiList = ViewBindings.findChildViewById(rootView, id);
      if (doubleButtonedTourokuRyoseiList == null) {
        break missingId;
      }

      id = R.id.double_buttoned_touroku_titile;
      TextView doubleButtonedTourokuTitile = ViewBindings.findChildViewById(rootView, id);
      if (doubleButtonedTourokuTitile == null) {
        break missingId;
      }

      id = R.id.double_jimutou_name_show;
      TextView doubleJimutouNameShow = ViewBindings.findChildViewById(rootView, id);
      if (doubleJimutouNameShow == null) {
        break missingId;
      }

      id = R.id.double_touroku_go_back_button;
      Button doubleTourokuGoBackButton = ViewBindings.findChildViewById(rootView, id);
      if (doubleTourokuGoBackButton == null) {
        break missingId;
      }

      return new DoubleButtonedTourokuLayoutBinding((ConstraintLayout) rootView,
          doubleButtonedTourokuBlockList, doubleButtonedTourokuRoomList,
          doubleButtonedTourokuRyoseiList, doubleButtonedTourokuTitile, doubleJimutouNameShow,
          doubleTourokuGoBackButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
