package common.util;

import org.junit.Test;

import common.util.Encryptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EncryptorTest {

    @Test
    public void testMD5() throws Exception {
        String str1 = "123456";
        String str2 = "654321";
        assertThat(Encryptor.MD5(str1), is("e10adc3949ba59abbe56e057f20f883e"));
        assertThat(Encryptor.MD5(str2), is("c33367701511b4f6020ec61ded352059"));
    }

    @Test
    public void testSHA1() {
        String str1 = "123456";
        assertThat(Encryptor.sha1(str1),
                is("7c4a8d09ca3762af61e59520943dc26494f8941b"));
    }
}
