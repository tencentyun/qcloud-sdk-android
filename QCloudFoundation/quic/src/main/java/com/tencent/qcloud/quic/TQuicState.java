package com.tencent.qcloud.quic;

public class TQuicState {
    public boolean isValid;        // if this stats is valid.
    public boolean isQuic;         // if it is quic, else it is tcp.
    public boolean is0rtt;         // Only valid if it is quic.
    public long connectMs;  // connect cost in millionseconds.
    public long ttfbMs;     // first byte cost from send request in millionseconds.

    // Only valid if it is quic.
    public long completeMs; // all bytes received cost from send request.
    public long srttUs;  // Smoothed RTT in microseconds.
    public long packetsSent;  // Number of packets be sent.
    public long packetsRetransmitted;  // Number of packets be retransmitted.

    public long bytesSent;
    public long bytesRetransmitted;
    public long packetsLost;  // Number of packets be lost when sent data.
    public long packetReceived; // Total packets received
    public long bytesReceived;  // Total bytes received including packet format.
    public long streamBytesReceived;  // Total bytes received including duplicated data.
}
