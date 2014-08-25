package common.util;

import org.junit.Test;

import common.util.Encryptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EncryptorTest {

    @Test
    public void testMD5() throws Exception {
        assertThat(Encryptor.md5("123456"), is("e10adc3949ba59abbe56e057f20f883e"));
        assertThat(Encryptor.md5("654321"), is("c33367701511b4f6020ec61ded352059"));
    }

    @Test
    public void testSHA1() {
        assertThat(Encryptor.sha1("123456"), is("7c4a8d09ca3762af61e59520943dc26494f8941b"));
    }
}
