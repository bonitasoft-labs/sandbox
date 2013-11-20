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
package org.bonitasoft.engine.poc.tenant;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    /**
     * Usage:
     * mvn -o compile exec:java -Dexec.mainClass=org.bonitasoft.engine.poc.Main -Dexec.args=
     * "file:///home/emmanuel/workspace/sandbox/classloader-pojo2/target/classloader-pojo2-0.0.1-SNAPSHOT.jar file:///home/emmanuel/workspace/sandbox/classloader-pojo1/target/classloader-pojo1-0.0.1-SNAPSHOT.jar"
     * mvn -o compile exec:java -Dexec.mainClass=org.bonitasoft.engine.poc.tenant.Main -Dexec.args=
     * "file:///home/matti/git/sandbox/classloader-poc/classloader-pojo1/target/classloader-pojo1-0.0.1-SNAPSHOT.jar file:///home/matti/git/sandbox/classloader-poc/classloader-pojo2/target/classloader-pojo2-0.0.1-SNAPSHOT.jar"
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
        } catch (final ClassNotFoundException e) {
            System.out.println("OK, class org.bonitasoft.engine.poc.pojo.LeaveRequest is not found. That is normal.");
        }
        UpdateTenant updateTenant = new UpdateTenant(args[0], new Tenant());
        Thread thread = new Thread(updateTenant);
        thread.start();
        thread.join();
        Tenant tenant = updateTenant.getTenant();

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        final int NB_THREAD_COUPLES = 5;
        for (int i = 0; i < NB_THREAD_COUPLES; i++) {
            final Thread thread2 = new Thread(new InsertEntity(tenant,
                    "import org.bonitasoft.engine.poc.pojo.LeaveRequest; LeaveRequest lr = new LeaveRequest(); lr.numberOfDays = 45; return lr;"));
            executorService.execute(thread2);
        }
        executorService.shutdown();
        executorService.awaitTermination(NB_THREAD_COUPLES, TimeUnit.SECONDS);

        tenant.stop();

        updateTenant = new UpdateTenant(args[1], tenant);
        thread = new Thread(updateTenant);
        thread.start();
        thread.join();
        tenant = updateTenant.getTenant();

        executorService = Executors.newFixedThreadPool(50);
        for (int i = 0; i < NB_THREAD_COUPLES; i++) {
            final Thread thread2 = new Thread(new InsertEntity(tenant,
                    "import org.bonitasoft.engine.poc.pojo.LeaveRequest; LeaveRequest lr = new LeaveRequest(); lr.leaveType = 'RTT'; return lr;"));
            executorService.execute(thread2);
        }
        executorService.shutdown();
        executorService.awaitTermination(NB_THREAD_COUPLES, TimeUnit.SECONDS);
    }

}
