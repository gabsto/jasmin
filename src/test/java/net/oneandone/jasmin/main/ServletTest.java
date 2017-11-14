/*
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.oneandone.jasmin.main;

import net.oneandone.sushi.fs.Node;
import net.oneandone.sushi.fs.World;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

public class ServletTest {
    @Test
    public void header() {
        check(true, null);
        check(false, "");
        check(true, "utf-8");
        check(true, "*");
        check(false, "utf-9");
        check(false, "utf-8;q=0");
        check(true, "utf-8;q=0.1");
        check(true, "utf-8;q=0.01");
        check(true, "utf-8;q=0, *");
        check(false, "utf-8;q=0, *;q=0");
        check(true, "utf-8;q=0, *;q=0.01");
        check(true, "*;q=0.01");
        check(true, "* ; q = 0.01");
        check(false, "* ; q  =  0");
    }

    private void check(boolean expected, String accepts) {
        boolean result;

        try {
            Servlet.checkCharset(accepts);
            result = true;
        } catch (IOException e) {
            result = false;
        }
        assertEquals(expected, result);
    }

    public static void main(String[] args) throws Exception {
        World world;
        Node node;
        int c;
        int count;

        world = World.create();
        node = world.node("http://dsl.1und1.de/xml/jasmin/get/111004-1208/prefix+dslorder-de+opener-detection+qx-clickmap+"
                + "qx-backbutton+econda-tracking+nedstat-tracking+adition-retargeting/js-min/AC:O:def");
        try (Reader reader = node.newReader()) {
            count = 0;
            Thread.sleep(10000);
            while ((c = reader.read()) != -1) {
                count++;
                System.out.print((char) c);
            }
            System.out.println("count: " + count);
        }
    }
}
