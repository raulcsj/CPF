/************************************************************************
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ************************************************************************/
package common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具。
 * 
 * @author CSJ
 * @email raulcsj@126.com
 * @create 2014年8月20日
 * @version 1.0
 */
public final class Encryptor {

    private static final int BYTE_H = 0XFF;

    private Encryptor() {
    }

    public static String md5(String src) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(src.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 digest error!", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not support!", e);
        }

        byte[] byteArray = md.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(BYTE_H & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(BYTE_H & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(BYTE_H & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    public static String sha1(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] result = md.digest(src.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & BYTE_H) + 0x100, 16)
                    .substring(1));
        }

        return sb.toString();
    }

}
