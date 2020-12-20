package edu.myblog.service;

import edu.myblog.utils.GenerateId;
import org.junit.Test;

public class GenerateIdTest {


    @Test
    public void testId() {
        System.out.println(GenerateId.createOnlyIdByHex());
    }

    @Test
    public void cutString() {
        String s = "qwda.jpg";
        String[] split = s.split("\\.");
        System.out.println(split[0]);
        System.out.println(split[1]);

    }
}
