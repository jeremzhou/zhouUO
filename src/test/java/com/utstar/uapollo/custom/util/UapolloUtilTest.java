package com.utstar.uapollo.custom.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author UTSC0167
 * @date 2018年4月17日
 *
 */
public class UapolloUtilTest {

    @Test
    public void testGetUnixTime() {
        assertThat(UapolloUtil.getUnixTime(),equalTo(System.currentTimeMillis()/1000));;
    }
}