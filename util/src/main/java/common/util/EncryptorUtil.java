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

import common.exception.app.AppRuntimeException;

/**
 * <p>
 * 密码加密工具。
 * </p>
 * 
 * @author CSJ (raulcsj@126.com)
 * @version 1.0
 */
public final class EncryptorUtil {

    private static final int BYTE_L = 0x0F;
    private static final int BYTE_H = 0xF0;
    private static final int BYTE_MAX = 0XFF;
    private static final int RADIX_16 = 0X10;
    private static final String HEX_SEQ = "0123456789ABCDEF";

    private EncryptorUtil() {
    }

    /**
     * MD5加密
     * 
     * @param src
     * @return
     */
    public static String md5(String src) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(src.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new AppRuntimeException("MD5 digest error!", e);
        } catch (UnsupportedEncodingException e) {
            throw new AppRuntimeException("UTF-8 not support!", e);
        }

        byte[] byteArray = md.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(BYTE_MAX & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(BYTE_MAX & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(BYTE_MAX & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    /**
     * SHA1加密
     * 
     * @param src
     * @return
     */
    public static String sha1(String src) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new AppRuntimeException(e);
        }

        byte[] result = md.digest(src.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & BYTE_MAX) + 0x100, RADIX_16).substring(1));
        }

        return sb.toString();
    }

    /**
     * 字符串转换为16进制字符串
     * 
     * @param str
     * @return
     */
    public static String str2Hex(String str) {
        String res = "";
        if (str != null && !"".equals(str)) {
            char[] hexChars = HEX_SEQ.toCharArray();
            StringBuilder builder = new StringBuilder();
            byte[] bytes = str.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                int b_h = (b & BYTE_H) >> 4, b_l = (b & BYTE_L);
                builder.append(hexChars[b_h]).append(hexChars[b_l]);
            }
            res = builder.toString();
        }
        return res;
    }

    /**
     * 将16进制字符串转换为字符串
     * 
     * @param hexStr
     * @return
     */
    public static String hex2Str(String hexStr) {
        String res = "";
        if (hexStr != null && !"".equals(hexStr)) {
            char[] hexChars = hexStr.toCharArray();
            byte[] orgin = new byte[hexChars.length / 2];
            for (int i = 0; i < orgin.length; i++) {
                int b = (HEX_SEQ.indexOf(hexChars[2 * i]) << 4 | HEX_SEQ.indexOf(hexChars[2 * i + 1]));
                orgin[i] = (byte) (b & BYTE_MAX);
            }
            res = new String(orgin);
        }
        return res;
    }
}
