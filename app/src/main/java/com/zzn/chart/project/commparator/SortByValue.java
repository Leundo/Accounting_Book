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

package com.zzn.chart.project.commparator;

import com.github.mikephil.charting.data.PieEntry;

import java.util.Comparator;

public class SortByValue implements Comparator {
    public int compare(Object o1,Object o2){
        PieEntry p1 = (PieEntry) o1;
        PieEntry p2 = (PieEntry) o2;
        if(p1.getValue()<p2.getValue()){
            return 1;
        }
        else {
            return -1;
        }
    }
}
