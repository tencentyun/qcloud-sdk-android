package com.tencent.qcloud.core.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Signer factory. */
public final class SignerFactory {

    private static final String COS_XML_SIGNER = "CosXmlSigner";

    private static final Map<String, Class<? extends QCloudSigner>> SIGNERS = new ConcurrentHashMap<>(5);
    private static final Map<String, QCloudSigner> SIGNER_INSTANCES = new ConcurrentHashMap<>(5);

    static {
        // Register the standard signer types.
        SIGNERS.put(COS_XML_SIGNER, COSXmlSigner.class);
    }

    /**
     * Private so you're not tempted to instantiate me.
     */
    private SignerFactory() {
    }

    /**
     * Register an implementation class for the given signer type.
     *
     * @param signerType The name of the signer type to register.
     * @param signerClass The class implementing the given signature protocol.
     */
    public static void registerSigner(
            final String signerType,
            final Class<? extends QCloudSigner> signerClass) {

        if (signerType == null) {
            throw new IllegalArgumentException("signerType cannot be null");
        }
        if (signerClass == null) {
            throw new IllegalArgumentException("signerClass cannot be null");
        }

        SIGNERS.put(signerType, signerClass);
    }

    /**
     * Register an signer instance for the given signer type.
     *
     * @param signerType The name of the signer type to register.
     * @param signer The instance implementing the given signature protocol.
     * @param <T> Class Type extends {@link QCloudSigner}.
     */
    public static <T extends QCloudSigner> void registerSigner(
            final String signerType, final T signer) {
        if (signerType == null) {
            throw new IllegalArgumentException("signerType cannot be null");
        }
        if (signer == null) {
            throw new IllegalArgumentException("signer instance cannot be null");
        }

        SIGNER_INSTANCES.put(signerType, signer);
    }

    /**
     *
     * @param signerType The signType to talk to.
     *
     * @return a non-null signer for the specified service and region according
     * to the internal configuration which provides a basic default algorithm
     * used for signer determination.
     */
    public static QCloudSigner getSigner(String signerType) {
        return lookupAndCreateSigner(signerType);
    }

    /**
     * Internal implementation for looking up and creating a signer by service
     * name and region.
     */
    private static QCloudSigner lookupAndCreateSigner(String signerType) {
        if (SIGNER_INSTANCES.containsKey(signerType)) {
            return SIGNER_INSTANCES.get(signerType);
        }
        return createSigner(signerType);
    }

    /**
     * Internal implementation to create a signer by type and service name, and
     * configuring it with the service name if applicable.
     */
    private static QCloudSigner createSigner(String signerType) {
        Class<? extends QCloudSigner> signerClass = SIGNERS.get(signerType);
        if (signerClass == null)
            return null;
        QCloudSigner signer;
        try {
            signer = signerClass.newInstance();
            SIGNER_INSTANCES.put(signerType, signer);
        } catch (InstantiationException ex) {
            throw new IllegalStateException(
                    "Cannot create an instance of " + signerClass.getName(),
                    ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                    "Cannot create an instance of " + signerClass.getName(),
                    ex);
        }

        return signer;
    }
}
