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

import com.leundo.data.Bill;

import java.util.ArrayList;

public class BrowseGroupEntity {
    private String headerTitle;
    private ArrayList<Bill> children;

    public BrowseGroupEntity(String headerTitle, ArrayList<Bill> children) {
        this.headerTitle = headerTitle;
        this.children = children;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public Bill getChildren(int childPosition) {
        return children.get(childPosition);
    }

    public ArrayList<Bill> getBillList() {
        return children;
    }

    public int size() {
        return children.size();
    }

    public void setChildren(ArrayList<Bill> children) {
        this.children = children;
    }
}
