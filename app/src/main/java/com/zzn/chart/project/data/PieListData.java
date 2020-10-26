/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zzn.chart.project.data;

public class PieListData {
    private Integer color;
    private String pieDataType;
    private double pieDataAmount;
    private double sumForProportion;

    public PieListData(Integer color, String pieDataType, double pieDataAmount, double sumForProportion) {
        this.color = color;
        this.pieDataType = pieDataType;
        this.pieDataAmount = pieDataAmount;
        this.sumForProportion = sumForProportion;
    }

    public Integer getColor() {
        return color;
    }
    public void setColor(Integer color) {
        this.color = color;
    }

    public String getPieDataType() {
        return pieDataType;
    }
    public void setPieDataType(String pieDataType) {
        this.pieDataType = pieDataType;
    }

    public double getPieDataAmount() {
        return pieDataAmount;
    }
    public void setPieDataAmount(double pieDataAmount) {
        this.pieDataAmount = pieDataAmount;
    }

    public double getSumForProportion(){return sumForProportion;}
    public void setSumForProportion(double sumForProportion){this.sumForProportion = sumForProportion;}

    public double getProportion(){
        return (pieDataAmount/sumForProportion*100);
    }
}
