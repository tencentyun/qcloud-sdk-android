package com.tencent.cos.xml.model.tag;

/**
 * Created by bradyxiao on 2017/12/29.
 */

public class RestoreConfigure {

    public int days;
    public CASJobParameters casJobParameters;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{RestoreRequest:\n");
        stringBuilder.append("Days:").append(days).append("\n");
        if(casJobParameters != null)stringBuilder.append(casJobParameters.toString()).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class CASJobParameters{
        public String tier = Tier.Standard.getTier();

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{CASJobParameters:\n");
            stringBuilder.append("Tier:").append(tier).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static enum Tier{
        Expedited("Expedited") ,
        Standard("Standard"),
        Bulk("Bulk");
        private String tier;
        Tier(String tier) {
            this.tier = tier;
        }

        public String getTier() {
            return tier;
        }
    }
}
