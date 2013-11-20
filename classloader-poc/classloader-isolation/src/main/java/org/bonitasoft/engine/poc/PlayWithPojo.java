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

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Emmanuel Duchastenier
 */
public class PlayWithPojo implements Runnable {

    private final String dir;

    /**
     * @param dir
     */
    public PlayWithPojo(final String dir) {
        this.dir = dir;
    }

    @Override
    public void run() {
        ClassLoader previousContextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader cl = null;
        try {
            cl = new URLClassLoader(new URL[] { new URL(dir) }, previousContextClassLoader);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        System.out.println("CL du PlayWithPojo: " + cl);
        try {
            Thread.currentThread().setContextClassLoader(cl);
            System.out.println("CL du PlayWithPojo: " + cl);
            System.out.println("Chargement d'une ressource: " + cl.getResource("org/bonitasoft/engine/poc/pojo/LeaveRequest.class").toString());
            Class<?> loadedClass;
            try {
                loadedClass = cl.loadClass("org.bonitasoft.engine.poc.pojo.LeaveRequest");
                // loadedClass = Class.forName("org.bonitasoft.engine.poc.pojo.LeaveRequest", true, cl);
                System.out.println("Class correctly loaded: " + loadedClass);
            } catch (Throwable e) {
                throw new RuntimeException("FAILED to load class", e);
            }

            Object object = loadedClass.newInstance();
            System.out.println("Class of type " + object.getClass().getName());
            Method method = loadedClass.getMethod("setNumberOfDays", int.class);
            method.invoke(object, 17);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(previousContextClassLoader);
        }
    }

}
