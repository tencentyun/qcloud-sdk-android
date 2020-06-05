package com.tencent.cos.xml.constraints.controller;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.impl.constraints.ConstraintListener;
import androidx.work.impl.constraints.trackers.ConstraintTracker;

import com.tencent.cos.xml.constraints.TransferSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rickenwang on 2019-11-27.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
@SuppressLint("RestrictedApi")
public abstract class ConstraintController<T> implements ConstraintListener<T> {


    /**
     * A callback for when a constraint changes.
     */
    public interface OnConstraintUpdatedCallback {

        /**
         * Called when a constraint is met.
         *
         * @param transferSpecIds A list of {@link TransferSpec} IDs that may have become eligible to run
         */
        void onConstraintMet(@NonNull List<String> transferSpecIds);

        /**
         * Called when a constraint is not met.
         *
         * @param transferSpecIds A list of {@link TransferSpec} IDs that have become ineligible to run
         */
        void onConstraintNotMet(@NonNull List<String> transferSpecIds);
    }

    private final List<String> mMatchingTransferSpecIds = new ArrayList<>();

    private T mCurrentValue;
    private ConstraintTracker<T> mTracker;
    private OnConstraintUpdatedCallback mCallback;

    public ConstraintController(ConstraintTracker<T> tracker) {
        mTracker = tracker;
    }

    /**
     * Sets the callback to inform when constraints change.  This callback is also triggered the
     * first time it is set.
     *
     * @param callback The callback to inform about constraint met/unmet states
     */
    public void setCallback(OnConstraintUpdatedCallback callback) {
        if (mCallback != callback) {
            mCallback = callback;
            updateCallback();
        }
    }

    abstract public boolean hasConstraint(@NonNull TransferSpec transferSpec);

    abstract public boolean isConstrained(@NonNull T currentValue);


    /**
     * Replaces the list of {@link TransferSpec}s to monitor constraints for.
     *
     * @param transferSpecs A list of {@link TransferSpec}s to monitor constraints for
     */

    public void replace(@NonNull List<TransferSpec> transferSpecs) {
        mMatchingTransferSpecIds.clear();

        for (TransferSpec transferSpec : transferSpecs) {
            if (hasConstraint(transferSpec)) {
                mMatchingTransferSpecIds.add(transferSpec.id);
            }
        }

        if (mMatchingTransferSpecIds.isEmpty()) {
            mTracker.removeListener(this);
        } else {
            mTracker.addListener(this);
        }
        updateCallback();
    }

    /**
     * Clears all tracked {@link TransferSpec}s.
     */
    public void reset() {
        if (!mMatchingTransferSpecIds.isEmpty()) {
            mMatchingTransferSpecIds.clear();
            mTracker.removeListener(this);
        }
    }

    /**
     * Determines if a particular {@link TransferSpec} is constrained. It is constrained if it is
     * tracked by this controller, and the controller constraint was set, but not satisfied.
     *
     * @param transferSpecId The ID of the {@link TransferSpec} to check if it is constrained.
     * @return {@code true} if the {@link TransferSpec} is considered constrained
     */
    public boolean isTransferSpecConstrained(@NonNull String transferSpecId) {
        return mCurrentValue != null && isConstrained(mCurrentValue)
                && mMatchingTransferSpecIds.contains(transferSpecId);
    }

    private void updateCallback() {
        if (mMatchingTransferSpecIds.isEmpty() || mCallback == null) {
            return;
        }

        if (mCurrentValue == null || isConstrained(mCurrentValue)) {
            mCallback.onConstraintNotMet(mMatchingTransferSpecIds);
        } else {
            mCallback.onConstraintMet(mMatchingTransferSpecIds);
        }
    }

    @Override
    public void onConstraintChanged(@Nullable T newValue) {
        mCurrentValue = newValue;
        updateCallback();
    }

}
