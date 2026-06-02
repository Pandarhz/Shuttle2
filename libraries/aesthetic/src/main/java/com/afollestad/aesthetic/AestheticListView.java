package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
public class AestheticListView extends ListView {

  private Disposable subscription;

  public AestheticListView(Context context) {
    super(context);
  }

  public AestheticListView(Context context, AttributeSet attrs) {
        AestheticListView.super.onDetachedFromWindow();
      }
    }, subscription);
  }
}
