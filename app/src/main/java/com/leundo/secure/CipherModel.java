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

package com.leundo.secure;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.MyApp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CipherModel {


    public static final String NUMBER_CIPHER_TITLE = "number_cipher";
    public static final String PATTERN_CIPHER_TITLE = "pattern_cipher";

    public static boolean isFirstSetting = true;

    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    public static String cipherToMd5(String dataStr) {
        try {
            dataStr = dataStr + slat;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // isExistedCipher(NUMBER_CIPHER_TITLE) 求数据库里面是否有数字密码
    // isExistedCipher(PATTERN_CIPHER_TITLE) 求数据库里面是否有图案密码
    public static boolean isExistedCipher(String cipherTitle) {
        BillDao billDao = new BillDao(MyApp.getContext());
        return billDao.isUserDataExisted(cipherTitle);
    }

    // 判断密码是否正确, rawCipher是明文密码
    public static boolean isCipherCorrect(String cipherTitle, String rawCipher) {
        BillDao billDao = new BillDao(MyApp.getContext());
        String processedCipher = billDao.queryUserData(cipherTitle);
        return  processedCipher != null && processedCipher.equals(cipherToMd5(rawCipher));
    }

    // 插入密码, 已经存在的会被替换
    public static void insertCipher(String cipherTitle, String rawCipher) {
        BillDao billDao = new BillDao(MyApp.getContext());
        billDao.insertOrUpdateUserData(cipherTitle, cipherToMd5(rawCipher));
    }


}
