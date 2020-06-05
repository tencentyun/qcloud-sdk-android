package com.tencent.cos.xml.transfer;

import java.util.HashMap;
import java.util.Map;

/**
 * The current state of a transfer. A transfer is initially in WAITING state
 * when added. It will turn into IN_PROGRESS once it starts. Customers can pause
 * or cancel the transfer when needed and turns it into PAUSED or CANCELED state
 * respectively. Finally the transfer will either succeed as COMPLETED or fail
 * as FAILED. WAITING_FOR_NETWORK state may kick in for an active transfer when
 * network is lost. The other enum values are internal use only.
 *
 * Created by rickenwang on 2018/5/16.
 * <p>
 * Copyright (c) 2010-2017 Tencent Cloud. All rights reserved.
 */

public enum TransferState {


    CONSTRAINED,

    /**
     * This state represents a transfer that has been queued, but has not yet
     * started
     * <br>
     */
    WAITING,

    /**
     * This state represents a transfer that is currently uploading or
     * downloading data
     */
    IN_PROGRESS,

    /**
     * This state represents a transfer that is paused manual
     */
    PAUSED,

    /**
     * This state represents a transfer that has been resumed and queued for
     * execution, but has not started to actively transfer data.
     * <br>
     */
    RESUMED_WAITING,

    /**
     * This state represents a transfer that is completed
     */
    COMPLETED,

    /**
     * This state represents a transfer that is canceled
     */
    CANCELED,

    /**
     * This state represents a transfer that has failed
     */
    FAILED,
    /**
     * This is an internal value used to detect if the current transfer is in an
     * unknown state
     */
    UNKNOWN;

    private static final Map<String, TransferState> MAP;
    static {
        MAP = new HashMap<String, TransferState>();
        for (final TransferState state : TransferState.values()) {
            MAP.put(state.toString(), state);
        }
    }


    /**
     * Returns the transfer state from string
     *
     * @param stateAsString state of the transfer represented as string.
     * @return the {@link TransferState}
     */
    public static TransferState getState(String stateAsString) {
        if (MAP.containsKey(stateAsString)) {
            return MAP.get(stateAsString);
        }

        return UNKNOWN;
    }
}

