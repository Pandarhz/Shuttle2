package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.resolveResId;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
public class AestheticSwitchCompat extends SwitchCompat {

  private Disposable subscription;
  private int backgroundResId;

  public AestheticSwitchCompat(Context context) {
    super(context);
  }

  public AestheticSwitchCompat(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AestheticSwitchCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        AestheticSwitchCompat.super.onDetachedFromWindow();
      }
    }, subscription);
  }
}
