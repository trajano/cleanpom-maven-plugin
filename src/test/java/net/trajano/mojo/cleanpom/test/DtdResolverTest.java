package net.trajano.mojo.cleanpom.test;


import org.junit.Assert;
import org.junit.Test;

import net.trajano.mojo.cleanpom.internal.DtdResolver;

public class DtdResolverTest {

    @Test
    public void testJustConstructed() {

        Assert.assertFalse(new DtdResolver().isDtdPresent());
    }
}
