package com.tencent.cos.xml.crypto;

import java.util.Map;

/**
 * <p>
 * Created by rickenwang on 2021/7/5.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public interface MaterialsDescriptionProvider {

    /**
     * Returns an unmodifiable view of the MaterialsDescription which the caller 
     * can use to load EncryptionMaterials from any {@link EncryptionMaterialsAccessor}
     * @return materials description.
     */
    public Map<String, String> getMaterialsDescription();
}
