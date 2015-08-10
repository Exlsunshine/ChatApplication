/*
 * Copyright (C) 2015 RECRUIT LIFESTYLE CO., LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.recruit_lifestyle.android.widget;

import com.example.testmobiledatabase.R;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

/**
 * @author amyu
 *
 * 波と落ちる円を描画するView
 */
public class WaveView extends View implements ViewTreeObserver.OnPreDrawListener {

  /**
   * {@link WaveView#mDropCircleAnimator} のDuration
   */
  private static final long DROP_CIRCLE_ANIMATOR_DURATION = 500;

  /**
   * {@link WaveView#mDropBounceVerticalAnimator} のDuration
   */
  private static final long DROP_VERTEX_ANIMATION_DURATION = 500;

  /**
   * {@link WaveView#mDropBounceVerticalAnimator} �?{@link WaveView#mDropBounceHorizontalAnimator}
   * のDuration
   */
  private static final long DROP_BOUNCE_ANIMATOR_DURATION = 500;

  /**
   * {@link WaveView#mDisappearCircleAnimator} のDuration
   */
  private static final int DROP_REMOVE_ANIMATOR_DURATION = 200;

  /**
   * 波がくねくねしているDuration
   */
  private static final int WAVE_ANIMATOR_DURATION = 1000;

  /**
   * 波の�?��の高�?   */
  private static final float MAX_WAVE_HEIGHT = 0.2f;

  /**
   * 影の�?   */
  private static final int SHADOW_COLOR = 0xFF000000;

  /**
   * 円のRadius
   */
  private float mDropCircleRadius = 100;

  /**
   * すべてを描画するPaint
   */
  private Paint mPaint;

  /**
   * 画面の波を描画するためのPath
   */
  private Path mWavePath;

  /**
   * 落ちる円の接線を描画するためのPath
   */
  private Path mDropTangentPath;

  /**
   * 落ちる円を描画するためのPath
   */
  private Path mDropCirclePath;

  /**
   * 影のPaint
   */
  private Paint mShadowPaint;

  /**
   * 影のPath
   */
  private Path mShadowPath;

  /**
   * 落ちる円の座標を入れているRectF
   */
  private RectF mDropRect;

  /**
   * Viewの横�?   */
  private int mWidth;

  /**
   * {@link WaveView#mDropCircleAnimator} でアニメーションしてる時の円の中心のY座標
   */
  private float mCurrentCircleCenterY;

  /**
   * 円が落ちる最大の高さ
   */
  private int mMaxDropHeight;

  private boolean mIsManualRefreshing = false;

  /**
   * 落ちる円の高さが更新されたかどう�?   */
  private boolean mDropHeightUpdated = false;

  /**
   * {@link WaveView#mMaxDropHeight} を更新するための�?��的な値の置き�?   */
  private int mUpdateMaxDropHeight;

  /**
   * 落ちてくる円についてくる三角形の�?��上の頂点のAnimator
   */
  private ValueAnimator mDropVertexAnimator;

  /**
   * 落ちた円が横に伸びるときのAnimator
   */
  private ValueAnimator mDropBounceVerticalAnimator;

  /**
   * 落ちた縁が縦に伸びるときのAnimator
   */
  private ValueAnimator mDropBounceHorizontalAnimator;

  /**
   * 落ちる円の中心座標のAnimator
   */
  private ValueAnimator mDropCircleAnimator;

  /**
   * 落ちた円を消すためのAnimator
   */
  private ValueAnimator mDisappearCircleAnimator;

  /**
   * 帰ってくる波ののAnimator
   */
  private ValueAnimator mWaveReverseAnimator;

  /**
   * ベジェ曲線を引く際の座標
   * 左側�?つのアンカーポイントでいい感じに右側にも
   */
  private static final float[][] BEGIN_PHASE_POINTS = {
      //1
      { 0.1655f, 0 },           //ハンドル
      { 0.4188f, -0.0109f },    //ハンドル
      { 0.4606f, -0.0049f },    //アンカーポイント

      //2
      { 0.4893f, 0.f },         //ハンドル
      { 0.4893f, 0.f },         //ハンドル
      { 0.5f, 0.f }             //アンカーポイント
  };

  private static final float[][] APPEAR_PHASE_POINTS = {
      //1
      { 0.1655f, 0.f },         //ハンドル
      { 0.5237f, 0.0553f },     //ハンドル
      { 0.4557f, 0.0936f },     //アンカーポイント

      //2
      { 0.3908f, 0.1302f },     //ハンドル
      { 0.4303f, 0.2173f },     //ハンドル
      { 0.5f, 0.2173f }         //アンカーポイント
  };

  private static final float[][] EXPAND_PHASE_POINTS = {
      //1
      { 0.1655f, 0.f },         //ハンドル
      { 0.5909f, 0.0000f },     //ハンドル
      { 0.4557f, 0.1642f },     //アンカーポイント

      //2
      { 0.3941f, 0.2061f },     //ハンドル
      { 0.4303f, 0.2889f },     //ハンドル
      { 0.5f, 0.2889f }         //アンカーポイント
  };

  /**
   * 各AnimatorのAnimatorUpdateListener
   */
  private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener =
      new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
          postInvalidate();
        }
      };

  /**
   * Constructor
   * {@inheritDoc}
   */
  public WaveView(Context context) {
    super(context);
    getViewTreeObserver().addOnPreDrawListener(this);
    initView();
  }

  /**
   * Viewのサイズが決まったら {@link WaveView#mWidth} に横�?   * {@inheritDoc}
   */
  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mWidth = w;
    mDropCircleRadius = w / 14.4f;
    updateMaxDropHeight((int) Math.min(Math.min(w, h), getHeight() - mDropCircleRadius));
    super.onSizeChanged(w, h, oldw, oldh);
  }

  /**
   * 描画されてか�?{@link WaveView#mMaxDropHeight} を更新す�?   * {@inheritDoc}
   */
  @Override public boolean onPreDraw() {
    getViewTreeObserver().removeOnPreDrawListener(this);
    if (mDropHeightUpdated) {
      updateMaxDropHeight(mUpdateMaxDropHeight);
    }
    return false;
  }

  @Override protected void onDraw(Canvas canvas) {
    //引っ張ってる�?��の波と終わったあとの�?    canvas.drawPath(mWavePath, mShadowPaint);
    canvas.drawPath(mWavePath, mPaint);
    mWavePath.reset();

    //円が落ちる部分の描画
    mDropTangentPath.reset();
    mDropCirclePath.reset();
    float circleCenterY = (Float) mDropCircleAnimator.getAnimatedValue();
    float circleCenterX = mWidth / 2.f;
    mDropRect.setEmpty();
    //円の座標をRectFに保�?    
    float scale = (Float) mDisappearCircleAnimator.getAnimatedValue();
    float vertical = (Float) mDropBounceVerticalAnimator.getAnimatedValue();
    float horizontal = (Float) mDropBounceHorizontalAnimator.getAnimatedValue();
    mDropRect.set(circleCenterX - mDropCircleRadius * (1 + vertical) * scale
            + mDropCircleRadius * horizontal / 2,
        circleCenterY + mDropCircleRadius * (1 + horizontal) * scale
            - mDropCircleRadius * vertical / 2,
        circleCenterX + mDropCircleRadius * (1 + vertical) * scale
            - mDropCircleRadius * horizontal / 2,
        circleCenterY - mDropCircleRadius * (1 + horizontal) * scale
            + mDropCircleRadius * vertical / 2);
    float vertex = (Float) mDropVertexAnimator.getAnimatedValue();
    mDropTangentPath.moveTo(circleCenterX, vertex);
    //円の接点(p1,q),(p2,q)
    double q =
        (Math.pow(mDropCircleRadius, 2) + circleCenterY * vertex - Math.pow(circleCenterY, 2)) / (
            vertex - circleCenterY);
    //2次方程式解くための解の公�?    
    double b = -2.0 * mWidth / 2;
    double c =
        Math.pow(q - circleCenterY, 2) + Math.pow(circleCenterX, 2) - Math.pow(mDropCircleRadius,
            2);
    double p1 = (-b + Math.sqrt(b * b - 4 * c)) / 2;
    double p2 = (-b - Math.sqrt(b * b - 4 * c)) / 2;
    mDropTangentPath.lineTo((float) p1, (float) q);
    mDropTangentPath.lineTo((float) p2, (float) q);
    mDropTangentPath.close();
    mShadowPath.set(mDropTangentPath);
    mShadowPath.addOval(mDropRect, Path.Direction.CCW);
    mDropCirclePath.addOval(mDropRect, Path.Direction.CCW);
    if (mDropVertexAnimator.isRunning()) {
      canvas.drawPath(mShadowPath, mShadowPaint);
    } else {
      canvas.drawPath(mDropCirclePath, mShadowPaint);
    }
    canvas.drawPath(mDropTangentPath, mPaint);
    canvas.drawPath(mDropCirclePath, mPaint);
  }

  @Override protected void onDetachedFromWindow() {
    if (mDisappearCircleAnimator != null) {
      mDisappearCircleAnimator.end();
      mDisappearCircleAnimator.removeAllUpdateListeners();
    }
    if (mDropCircleAnimator != null) {
      mDropCircleAnimator.end();
      mDropCircleAnimator.removeAllUpdateListeners();
    }
    if (mDropVertexAnimator != null) {
      mDropVertexAnimator.end();
      mDropVertexAnimator.removeAllUpdateListeners();
    }
    if (mWaveReverseAnimator != null) {
      mWaveReverseAnimator.end();
      mWaveReverseAnimator.removeAllUpdateListeners();
    }
    if (mDropBounceHorizontalAnimator != null) {
      mDropBounceHorizontalAnimator.end();
      mDropBounceHorizontalAnimator.removeAllUpdateListeners();
    }
    if (mDropBounceVerticalAnimator != null) {
      mDropBounceVerticalAnimator.end();
      mDropBounceVerticalAnimator.removeAllUpdateListeners();
    }
    super.onDetachedFromWindow();
  }

  private void initView() {
    setUpPaint();
    setUpPath();
    resetAnimator();

    mDropRect = new RectF();
    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
  }

  private void setUpPaint() {
    mPaint = new Paint();
    mPaint.setColor(0xFF1E90FF);
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL);

    mShadowPaint = new Paint();
    mShadowPaint.setShadowLayer(10.0f, 0.0f, 2.0f, SHADOW_COLOR);
  }

  private void setUpPath() {
    mWavePath = new Path();
    mDropTangentPath = new Path();
    mDropCirclePath = new Path();
    mShadowPath = new Path();
  }

  private void resetAnimator() {
    mDropVertexAnimator = ValueAnimator.ofFloat(0.f, 0.f);
    mDropBounceVerticalAnimator = ValueAnimator.ofFloat(0.f, 0.f);
    mDropBounceHorizontalAnimator = ValueAnimator.ofFloat(0.f, 0.f);
    mDropCircleAnimator = ValueAnimator.ofFloat(-1000.f, -1000.f);
    mDropCircleAnimator.start();
    mDisappearCircleAnimator = ValueAnimator.ofFloat(1.f, 1.f);
    mDisappearCircleAnimator.setDuration(1); // immediately finish animation cycle
    mDisappearCircleAnimator.start();
  }

  private void onPreDragWave() {
    if (mWaveReverseAnimator != null) {
      if (mWaveReverseAnimator.isRunning()) {
        mWaveReverseAnimator.cancel();
      }
    }
  }

  public void manualRefresh() {
    if (mIsManualRefreshing) {
      return;
    }
    mIsManualRefreshing = true;
    mDropCircleAnimator = ValueAnimator.ofFloat(mMaxDropHeight, mMaxDropHeight);
    mDropCircleAnimator.start();
    mDropVertexAnimator = ValueAnimator.ofFloat(mMaxDropHeight - mDropCircleRadius,
        mMaxDropHeight - mDropCircleRadius);
    mDropVertexAnimator.start();
    mCurrentCircleCenterY = mMaxDropHeight;
    postInvalidate();
  }

  public void beginPhase(float move1) {
    onPreDragWave();
    //円を描画し始める前の引っ張ったら膨れる波の部分の描画
    mWavePath.moveTo(0, 0);
    //左半分の描画
    mWavePath.cubicTo(mWidth * BEGIN_PHASE_POINTS[0][0], BEGIN_PHASE_POINTS[0][1],
        mWidth * BEGIN_PHASE_POINTS[1][0], mWidth * (BEGIN_PHASE_POINTS[1][1] + move1),
        mWidth * BEGIN_PHASE_POINTS[2][0], mWidth * (BEGIN_PHASE_POINTS[2][1] + move1));
    mWavePath.cubicTo(mWidth * BEGIN_PHASE_POINTS[3][0],
        mWidth * (BEGIN_PHASE_POINTS[3][1] + move1), mWidth * BEGIN_PHASE_POINTS[4][0],
        mWidth * (BEGIN_PHASE_POINTS[4][1] + move1), mWidth * BEGIN_PHASE_POINTS[5][0],
        mWidth * (BEGIN_PHASE_POINTS[5][1] + move1));
    //右半分の描画
    mWavePath.cubicTo(mWidth - mWidth * BEGIN_PHASE_POINTS[4][0],
        mWidth * (BEGIN_PHASE_POINTS[4][1] + move1), mWidth - mWidth * BEGIN_PHASE_POINTS[3][0],
        mWidth * (BEGIN_PHASE_POINTS[3][1] + move1), mWidth - mWidth * BEGIN_PHASE_POINTS[2][0],
        mWidth * (BEGIN_PHASE_POINTS[2][1] + move1));
    mWavePath.cubicTo(mWidth - mWidth * BEGIN_PHASE_POINTS[1][0],
        mWidth * (BEGIN_PHASE_POINTS[1][1] + move1), mWidth - mWidth * BEGIN_PHASE_POINTS[0][0],
        BEGIN_PHASE_POINTS[0][1], mWidth, 0);
    ViewCompat.postInvalidateOnAnimation(this);
  }

  public void appearPhase(float move1, float move2) {
    onPreDragWave();
    mWavePath.moveTo(0, 0);
    //左半分の描画
    mWavePath.cubicTo(mWidth * APPEAR_PHASE_POINTS[0][0], mWidth * APPEAR_PHASE_POINTS[0][1],
        mWidth * Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, APPEAR_PHASE_POINTS[2][0]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]));
    mWavePath.cubicTo(
        mWidth * Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]),
        mWidth * Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, APPEAR_PHASE_POINTS[4][0]),
        mWidth * Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]),
        mWidth * APPEAR_PHASE_POINTS[5][0],
        mWidth * Math.min(BEGIN_PHASE_POINTS[0][1] + move1 + move2, APPEAR_PHASE_POINTS[5][1]));
    //右半分の描画
    mWavePath.cubicTo(
        mWidth - mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, APPEAR_PHASE_POINTS[4][0]),
        mWidth * Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]),
        mWidth - mWidth * Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]),
        mWidth * Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]),
        mWidth - mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, APPEAR_PHASE_POINTS[2][0]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]));
    mWavePath.cubicTo(
        mWidth - mWidth * Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]),
        mWidth - mWidth * APPEAR_PHASE_POINTS[0][0], mWidth * APPEAR_PHASE_POINTS[0][1], mWidth, 0);
    mCurrentCircleCenterY =
        mWidth * Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1])
            + mDropCircleRadius;
    ViewCompat.postInvalidateOnAnimation(this);
  }

  public void expandPhase(float move1, float move2, float move3) {
    onPreDragWave();
    mWavePath.moveTo(0, 0);
    //左半分の描画
    mWavePath.cubicTo(mWidth * EXPAND_PHASE_POINTS[0][0], mWidth * EXPAND_PHASE_POINTS[0][1],
        mWidth * Math.min(
            Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]) + move3,
            EXPAND_PHASE_POINTS[1][0]), mWidth * Math.max(
            Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]) - move3,
            EXPAND_PHASE_POINTS[1][1]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, EXPAND_PHASE_POINTS[2][0]),
        mWidth * Math.min(
            Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]) + move3,
            EXPAND_PHASE_POINTS[2][1]));
    mWavePath.cubicTo(mWidth * Math.min(
            Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]) + move3,
            EXPAND_PHASE_POINTS[3][0]), mWidth * Math.min(
            Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]) + move3,
            EXPAND_PHASE_POINTS[3][1]),
        mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, EXPAND_PHASE_POINTS[4][0]),
        mWidth * Math.min(
            Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]) + move3,
            EXPAND_PHASE_POINTS[4][1]), mWidth * EXPAND_PHASE_POINTS[5][0], mWidth * Math.min(
            Math.min(BEGIN_PHASE_POINTS[0][1] + move1 + move2, APPEAR_PHASE_POINTS[5][1]) + move3,
            EXPAND_PHASE_POINTS[5][1]));

    //右半分の描画
    mWavePath.cubicTo(
        mWidth - mWidth * Math.max(BEGIN_PHASE_POINTS[4][0] - move2, EXPAND_PHASE_POINTS[4][0]),
        mWidth * Math.min(
            Math.min(BEGIN_PHASE_POINTS[4][1] + move1 + move2, APPEAR_PHASE_POINTS[4][1]) + move3,
            EXPAND_PHASE_POINTS[4][1]), mWidth - mWidth * Math.min(
            Math.max(BEGIN_PHASE_POINTS[3][0] - move2, APPEAR_PHASE_POINTS[3][0]) + move3,
            EXPAND_PHASE_POINTS[3][0]), mWidth * Math.min(
            Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]) + move3,
            EXPAND_PHASE_POINTS[3][1]),
        mWidth - mWidth * Math.max(BEGIN_PHASE_POINTS[2][0] - move2, EXPAND_PHASE_POINTS[2][0]),
        mWidth * Math.min(
            Math.max(BEGIN_PHASE_POINTS[2][1] + move1 - move2, APPEAR_PHASE_POINTS[2][1]) + move3,
            EXPAND_PHASE_POINTS[2][1]));
    mWavePath.cubicTo(mWidth - mWidth * Math.min(
            Math.min(BEGIN_PHASE_POINTS[1][0] + move2, APPEAR_PHASE_POINTS[1][0]) + move3,
            EXPAND_PHASE_POINTS[1][0]), mWidth * Math.max(
            Math.max(BEGIN_PHASE_POINTS[1][1] + move1 - move2, APPEAR_PHASE_POINTS[1][1]) - move3,
            EXPAND_PHASE_POINTS[1][1]), mWidth - mWidth * EXPAND_PHASE_POINTS[0][0],
        mWidth * EXPAND_PHASE_POINTS[0][1], mWidth, 0);
    mCurrentCircleCenterY = mWidth * Math.min(
        Math.min(BEGIN_PHASE_POINTS[3][1] + move1 + move2, APPEAR_PHASE_POINTS[3][1]) + move3,
        EXPAND_PHASE_POINTS[3][1]) + mDropCircleRadius;
    ViewCompat.postInvalidateOnAnimation(this);
  }

  /**
   * @param height 高さ
   */
  private void updateMaxDropHeight(int height) {
    if (500 * (mWidth / 1440.f) > height) {
      Log.w("WaveView", "DropHeight is more than " + 500 * (mWidth / 1440.f));
      return;
    }
    mMaxDropHeight = (int) Math.min(height, getHeight() - mDropCircleRadius);
    if (mIsManualRefreshing) {
      mIsManualRefreshing = false;
      manualRefresh();
    }
  }

  public void startDropAnimation() {
	  
	  mDisappearCircleAnimator = ValueAnimator.ofFloat(1.f, 1.f);
		mDisappearCircleAnimator.setDuration(1); 
		mDisappearCircleAnimator.start();
		
		
    mDropCircleAnimator = ValueAnimator.ofFloat(500 * (mWidth / 1440.f), mMaxDropHeight);
    mDropCircleAnimator.setDuration(DROP_CIRCLE_ANIMATOR_DURATION);
    mDropCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mCurrentCircleCenterY = (Float) animation.getAnimatedValue();
        ViewCompat.postInvalidateOnAnimation(WaveView.this);
      }
    });
    mDropCircleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    mDropCircleAnimator.start();

    mDropVertexAnimator = ValueAnimator.ofFloat(0.f, mMaxDropHeight - mDropCircleRadius);
    mDropVertexAnimator.setDuration(DROP_VERTEX_ANIMATION_DURATION);
    mDropVertexAnimator.addUpdateListener(mAnimatorUpdateListener);
    mDropVertexAnimator.start();

    mDropBounceVerticalAnimator = ValueAnimator.ofFloat(0.f, 1.f);
    mDropBounceVerticalAnimator.setDuration(DROP_BOUNCE_ANIMATOR_DURATION);
    mDropBounceVerticalAnimator.addUpdateListener(mAnimatorUpdateListener);
    mDropBounceVerticalAnimator.setInterpolator(new DropBounceInterpolator());
    mDropBounceVerticalAnimator.setStartDelay(DROP_VERTEX_ANIMATION_DURATION);
    mDropBounceVerticalAnimator.start();

    mDropBounceHorizontalAnimator = ValueAnimator.ofFloat(0.f, 1.f);
    mDropBounceHorizontalAnimator.setDuration(DROP_BOUNCE_ANIMATOR_DURATION);
    mDropBounceHorizontalAnimator.addUpdateListener(mAnimatorUpdateListener);
    mDropBounceHorizontalAnimator.setInterpolator(new DropBounceInterpolator());
    mDropBounceHorizontalAnimator.setStartDelay(
        (long) (DROP_VERTEX_ANIMATION_DURATION + DROP_BOUNCE_ANIMATOR_DURATION * 0.25));
    mDropBounceHorizontalAnimator.start();
  }

  public void startDisappearCircleAnimation() {
    mDisappearCircleAnimator = ValueAnimator.ofFloat(1.f, 0.f);
    mDisappearCircleAnimator.addUpdateListener(mAnimatorUpdateListener);
    mDisappearCircleAnimator.setDuration(DROP_REMOVE_ANIMATOR_DURATION);
    mDisappearCircleAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {

      }

      @Override public void onAnimationEnd(Animator animator) {
        //アニメーション修旅時にAnimatorをリセットすることにより落ちてくる円の初期位置を-100.fにす�?        resetAnimator();
        mIsManualRefreshing = false;
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    mDisappearCircleAnimator.start();
  }

  /**
   * @param h 波が始まる高�?   */
  public void startWaveAnimation(float h) {
    h = Math.min(h, MAX_WAVE_HEIGHT) * mWidth;
    mWaveReverseAnimator = ValueAnimator.ofFloat(h, 0.f);
    mWaveReverseAnimator.setDuration(WAVE_ANIMATOR_DURATION);
    mWaveReverseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float h = (Float) valueAnimator.getAnimatedValue();
        mWavePath.moveTo(0, 0);
        mWavePath.quadTo(0.25f * mWidth, 0, 0.333f * mWidth, h * 0.5f);
        mWavePath.quadTo(mWidth * 0.5f, h * 1.4f, 0.666f * mWidth, h * 0.5f);
        mWavePath.quadTo(0.75f * mWidth, 0, mWidth, 0);
        postInvalidate();
      }
    });
    mWaveReverseAnimator.setInterpolator(new BounceInterpolator());
    mWaveReverseAnimator.start();
  }

  public void animationDropCircle() {
    if (mDisappearCircleAnimator.isRunning()) {
      return;
    }
    startDropAnimation();
    startWaveAnimation(0.1f);
  }

  public float getCurrentCircleCenterY() {
    return mCurrentCircleCenterY;
  }

  /**
   * @param maxDropHeight ある程度の高�?   */
  public void setMaxDropHeight(int maxDropHeight) {
    if (mDropHeightUpdated) {
      updateMaxDropHeight(maxDropHeight);
    } else {
      mUpdateMaxDropHeight = maxDropHeight;
      mDropHeightUpdated = true;
    }
  }

  public boolean isDisappearCircleAnimatorRunning() {
    return mDisappearCircleAnimator.isRunning();
  }

  /**
   * @param radius 影の深さ
   */
  public void setShadowRadius(int radius) {
    mShadowPaint.setShadowLayer(radius, 0.0f, 2.0f, SHADOW_COLOR);
  }
}
