package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
public class AestheticViewPager extends ViewPager {

  private Disposable subscription;

  public AestheticViewPager(Context context) {
    super(context);
  }

  public AestheticViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
        AestheticViewPager.super.onDetachedFromWindow();
      }
    }, subscription);
  }
}
