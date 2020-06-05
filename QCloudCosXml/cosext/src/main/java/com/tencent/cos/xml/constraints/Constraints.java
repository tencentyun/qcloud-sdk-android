package com.tencent.cos.xml.constraints;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.room.ColumnInfo;
import androidx.work.NetworkType;
import androidx.work.WorkRequest;

import static androidx.work.NetworkType.NOT_REQUIRED;

/**
 * Created by rickenwang on 2019-11-28.
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public final class Constraints {

    /**
     * Represents a Constraints object with no requirements.
     */
    public static final Constraints NONE = new Constraints.Builder().build();

    public static final Constraints NETWORK_ANY = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build();

    // NOTE: this is effectively a @NonNull, but changing the annotation would result in a really
    // annoying database migration that we can deal with later.
    @ColumnInfo(name = "required_network_type")
    private NetworkType mRequiredNetworkType = NOT_REQUIRED;

    @ColumnInfo(name = "requires_charging")
    private boolean mRequiresCharging;

    @ColumnInfo(name = "requires_device_idle")
    private boolean mRequiresDeviceIdle;

    @ColumnInfo(name = "requires_battery_not_low")
    private boolean mRequiresBatteryNotLow;

    @ColumnInfo(name = "requires_storage_not_low")
    private boolean mRequiresStorageNotLow;

    /**
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public Constraints() { // stub required for room
    }

    Constraints(Constraints.Builder builder) {
        mRequiresCharging = builder.mRequiresCharging;
        mRequiresDeviceIdle = Build.VERSION.SDK_INT >= 23 && builder.mRequiresDeviceIdle;
        mRequiredNetworkType = builder.mRequiredNetworkType;
        mRequiresBatteryNotLow = builder.mRequiresBatteryNotLow;
        mRequiresStorageNotLow = builder.mRequiresStorageNotLow;
    }

    public Constraints(@NonNull Constraints other) {
        mRequiresCharging = other.mRequiresCharging;
        mRequiresDeviceIdle = other.mRequiresDeviceIdle;
        mRequiredNetworkType = other.mRequiredNetworkType;
        mRequiresBatteryNotLow = other.mRequiresBatteryNotLow;
        mRequiresStorageNotLow = other.mRequiresStorageNotLow;
    }

    public @NonNull NetworkType getRequiredNetworkType() {
        return mRequiredNetworkType;
    }

    /**
     * Needed by Room.
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public void setRequiredNetworkType(@NonNull NetworkType requiredNetworkType) {
        mRequiredNetworkType = requiredNetworkType;
    }

    /**
     * @return {@code true} if the work should only execute while the device is charging
     */
    public boolean requiresCharging() {
        return mRequiresCharging;
    }

    /**
     * Needed by Room.
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public void setRequiresCharging(boolean requiresCharging) {
        mRequiresCharging = requiresCharging;
    }

    /**
     * @return {@code true} if the work should only execute while the device is idle
     */
    @RequiresApi(23)
    public boolean requiresDeviceIdle() {
        return mRequiresDeviceIdle;
    }

    /**
     * Needed by Room.
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @RequiresApi(23)
    public void setRequiresDeviceIdle(boolean requiresDeviceIdle) {
        mRequiresDeviceIdle = requiresDeviceIdle;
    }

    /**
     * @return {@code true} if the work should only execute when the battery isn't low
     */
    public boolean requiresBatteryNotLow() {
        return mRequiresBatteryNotLow;
    }

    /**
     * Needed by Room.
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public void setRequiresBatteryNotLow(boolean requiresBatteryNotLow) {
        mRequiresBatteryNotLow = requiresBatteryNotLow;
    }

    /**
     * @return {@code true} if the work should only execute when the storage isn't low
     */
    public boolean requiresStorageNotLow() {
        return mRequiresStorageNotLow;
    }

    /**
     * Needed by Room.
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public void setRequiresStorageNotLow(boolean requiresStorageNotLow) {
        mRequiresStorageNotLow = requiresStorageNotLow;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Constraints that = (Constraints) o;

        if (mRequiresCharging != that.mRequiresCharging) return false;
        if (mRequiresDeviceIdle != that.mRequiresDeviceIdle) return false;
        if (mRequiresBatteryNotLow != that.mRequiresBatteryNotLow) return false;
        if (mRequiresStorageNotLow != that.mRequiresStorageNotLow) return false;
        return (mRequiredNetworkType == that.mRequiredNetworkType);
    }

    @Override
    public int hashCode() {
        int result = mRequiredNetworkType.hashCode();
        result = 31 * result + (mRequiresCharging ? 1 : 0);
        result = 31 * result + (mRequiresDeviceIdle ? 1 : 0);
        result = 31 * result + (mRequiresBatteryNotLow ? 1 : 0);
        result = 31 * result + (mRequiresStorageNotLow ? 1 : 0);
        return result;
    }

    /**
     * A Builder for a {@link Constraints} object.
     */
    public static final class Builder {
        boolean mRequiresCharging = false;
        boolean mRequiresDeviceIdle = false;
        NetworkType mRequiredNetworkType = NOT_REQUIRED;
        boolean mRequiresBatteryNotLow = false;
        boolean mRequiresStorageNotLow = false;

        /**
         * Sets whether device should be charging for the {@link WorkRequest} to run.  The
         * default value is {@code false}.
         *
         * @param requiresCharging {@code true} if device must be charging for the work to run
         * @return The current {@link androidx.work.Constraints.Builder}
         */
        public @NonNull
        Constraints.Builder setRequiresCharging(boolean requiresCharging) {
            this.mRequiresCharging = requiresCharging;
            return this;
        }

        /**
         * Sets whether device should be idle for the {@link WorkRequest} to run.  The default
         * value is {@code false}.
         *
         * @param requiresDeviceIdle {@code true} if device must be idle for the work to run
         * @return The current {@link androidx.work.Constraints.Builder}
         */
        @RequiresApi(23)
        public @NonNull
        Constraints.Builder setRequiresDeviceIdle(boolean requiresDeviceIdle) {
            this.mRequiresDeviceIdle = requiresDeviceIdle;
            return this;
        }

        /**
         * Sets whether device should have a particular {@link NetworkType} for the
         * {@link WorkRequest} to run.  The default value is {@link NetworkType#NOT_REQUIRED}.
         *
         * @param networkType The type of network required for the work to run
         * @return The current {@link androidx.work.Constraints.Builder}
         */
        public @NonNull
        Constraints.Builder setRequiredNetworkType(@NonNull NetworkType networkType) {
            this.mRequiredNetworkType = networkType;
            return this;
        }

        /**
         * Sets whether device battery should be at an acceptable level for the
         * {@link WorkRequest} to run.  The default value is {@code false}.
         *
         * @param requiresBatteryNotLow {@code true} if the battery should be at an acceptable level
         *                              for the work to run
         * @return The current {@link androidx.work.Constraints.Builder}
         */
        public @NonNull
        Constraints.Builder setRequiresBatteryNotLow(boolean requiresBatteryNotLow) {
            this.mRequiresBatteryNotLow = requiresBatteryNotLow;
            return this;
        }

        /**
         * Sets whether the device's available storage should be at an acceptable level for the
         * {@link WorkRequest} to run.  The default value is {@code false}.
         *
         * @param requiresStorageNotLow {@code true} if the available storage should not be below a
         *                              a critical threshold for the work to run
         * @return The current {@link Constraints.Builder}
         */
        public @NonNull
        Constraints.Builder setRequiresStorageNotLow(boolean requiresStorageNotLow) {
            this.mRequiresStorageNotLow = requiresStorageNotLow;
            return this;
        }

        /**
         * Generates the {@link Constraints} from this Builder.
         *
         * @return The {@link Constraints} specified by this Builder
         */
        public @NonNull
        Constraints build() {
            return new Constraints(this);
        }
    }
}
