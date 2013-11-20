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

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Emmanuel Duchastenier
 */
public class Main {

    /**
     * Usage:
     * mvn -o compile exec:java -Dexec.mainClass=org.bonitasoft.engine.poc.Main -Dexec.args=
     * "file:///home/emmanuel/workspace/sandbox/classloader-pojo2/target/classloader-pojo2-0.0.1-SNAPSHOT.jar file:///home/emmanuel/workspace/sandbox/classloader-pojo1/target/classloader-pojo1-0.0.1-SNAPSHOT.jar"
     * 
     * @param args
     * @throws MalformedURLException
     * @throws InterruptedException
     */
    public static void main(final String[] args) throws MalformedURLException, InterruptedException {
        System.out.println("ClassLoader du main: " + Thread.currentThread().getContextClassLoader());
        try {
            Class.forName("org.bonitasoft.engine.poc.pojo.LeaveRequest");
            System.out.println("Class org.bonitasoft.engine.poc.pojo.LeaveRequest should not have been found. Exiting...");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("OK, class org.bonitasoft.engine.poc.pojo.LeaveRequest is not found. That is normal.");
        }
        UncaughtExceptionHandler exceHandler = new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                System.err.println("Unknown Throwable encountered in thread " + t);
                System.err.println("=================");
                e.printStackTrace();
                System.err.println("=================");
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        final int NB_THREAD_COUPLES = 150;
        for (int i = 0; i < NB_THREAD_COUPLES; i++) {
            // Thread thread1 = new Thread(new PlayWithGojo(args[0]));
            Thread thread1 = new Thread(new PlayWithHiberGojo(args[0],
                    "import org.bonitasoft.engine.poc.pojo.LeaveRequest; LeaveRequest lr = new LeaveRequest(); lr.leaveType = 'RTT'; return lr;"));
            executorService.execute(thread1);
            Thread thread2 = new Thread(new PlayWithHiberGojo(args[1],
                    "import org.bonitasoft.engine.poc.pojo.LeaveRequest; LeaveRequest lr = new LeaveRequest(); lr.numberOfDays = 45; return lr;"));
            executorService.execute(thread2);
            // System.out.println("ClassLoader du thread 1: " + thread1.getContextClassLoader());
            thread1.setUncaughtExceptionHandler(exceHandler);
        }
        executorService.shutdown();
        executorService.awaitTermination(NB_THREAD_COUPLES, TimeUnit.SECONDS);
    }

}
