package com.yalantis;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Artem Kholodnyi on 12/19/16.
 */
public class ExampleTest {

    @Test
    public void exampleTrue() {
        final int expected = 4;
        final int actual = 2 + 2;
        Assert.assertEquals(expected, actual);
    }
}
