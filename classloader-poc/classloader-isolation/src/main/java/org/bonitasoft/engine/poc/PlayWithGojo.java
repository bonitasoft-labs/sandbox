/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.engine.poc;

import groovy.lang.GroovyShell;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Emmanuel Duchastenier
 */
public class PlayWithGojo implements Runnable {

    private final String dir;

    private final static String GRROVY_SCRIPT = "import org.bonitasoft.engine.poc.pojo.LeaveRequest;\n" + "LeaveRequest lr = new LeaveRequest();\n"
            + "lr.leaveType = \"RTT\";\n" + "return lr;";

    /**
     * @param dir
     */
    public PlayWithGojo(final String dir) {
        this.dir = dir;
    }

    @Override
    public void run() {
        ClassLoader previousContextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader cl = null;
        try {
            cl = new URLClassLoader(new URL[] { new URL(dir) }, previousContextClassLoader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().setContextClassLoader(cl);
            GroovyShell shell = new GroovyShell(cl);
            Object object = shell.evaluate(GRROVY_SCRIPT);
            System.out.println("returned object from Groovy script: " + object);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(previousContextClassLoader);
        }
    }

}
