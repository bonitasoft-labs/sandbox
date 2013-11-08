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
package com.bonitasoft.example.process;

import java.util.List;

import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

public class TaskExecutorThread extends Thread {

    private Throwable exception;

    private String username;

    private String password;

    private int numberOfExecutedTasks = 0;

    public TaskExecutorThread(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        super.run();
        APISession session = null;
        LoginAPI loginAPI;
        try {
            loginAPI = TenantAPIAccessor.getLoginAPI();
        } catch (BonitaException e) {
            exception = e;
            return;
        }

        try {
            session = loginAPI.login(username, password);
            ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(session);

            HumanTaskInstance taskToExecute = waitForATask(session, processAPI);
            
            System.out.println("Assigning task " + taskToExecute.getName() + " to user " + session.getUserName() + "...");
            processAPI.assignUserTask(taskToExecute.getId(), session.getUserId());

            System.out.println("Executing task " + taskToExecute.getName() + "... ");
            processAPI.executeFlowNode(taskToExecute.getId());
            numberOfExecutedTasks++;
            System.out.println("Task " + taskToExecute.getName() + " executed!");
        } catch (Exception e) {
            exception = e;
        } finally {
            logout(session, loginAPI);
        }
    }

    private HumanTaskInstance waitForATask(APISession session, ProcessAPI processAPI) throws InterruptedException {
        List<HumanTaskInstance> pendingTasks = null;
        do {
            pendingTasks = processAPI.getPendingHumanTaskInstances(session.getUserId(), 0, 1, ActivityInstanceCriterion.PRIORITY_ASC);
            if(pendingTasks.isEmpty()) {
                Thread.sleep(500);
            }
        } while (pendingTasks.isEmpty());
        HumanTaskInstance taskToExecute = pendingTasks.get(0);
        return taskToExecute;
    }

    private void logout(APISession session, LoginAPI loginAPI) {
        if (session != null) {
            try {
                loginAPI.logout(session);
            } catch (BonitaException e) {
                exception = e;
            }
        }
    }

    public Throwable getException() {
        return exception;
    }

    public int getNumberOfExecutedTasks() {
        return numberOfExecutedTasks;
    }

}
