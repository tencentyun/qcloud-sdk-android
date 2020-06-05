package com.tencent.cos.xml.model.tag;

import java.util.Set;

public class ListInventoryConfiguration {
    public Set<InventoryConfiguration> inventoryConfigurations;
    public boolean isTruncated = false;
    public String continuationToken;
    public String nextContinuationToken;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{ListInventoryConfigurationResult\n");
        stringBuilder.append("IsTruncated:").append(isTruncated).append("\n");
        if(continuationToken != null)stringBuilder.append("ContinuationToken:").append(continuationToken).append("\n");
        if(nextContinuationToken != null)stringBuilder.append("NextContinuationToken:").append(nextContinuationToken).append("\n");
        if(inventoryConfigurations != null){
            for(InventoryConfiguration inventoryConfiguration : inventoryConfigurations){
                if(inventoryConfiguration != null)stringBuilder.append(inventoryConfiguration.toString()).append("\n");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
