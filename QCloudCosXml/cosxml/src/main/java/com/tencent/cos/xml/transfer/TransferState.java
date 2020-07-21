/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.transfer;

import java.util.HashMap;
import java.util.Map;

/**
 * 传输状态
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

