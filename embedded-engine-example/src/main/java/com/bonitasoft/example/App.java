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
package com.bonitasoft.example;

import javax.naming.Context;

import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.platform.PlatformLogoutException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.engine.session.SessionNotFoundException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import organization.OrganizationManager;

import com.bonitasoft.example.platform.PlatformManager;
import com.bonitasoft.example.process.ProcessManager;
import com.bonitasoft.example.process.TaskExecutorThread;

public class App {

    private static final String BONITA_HOME_PROPERTY = "bonita.home";

    private static final String BONITA_HOME_LOCATION = "bonita_home-6.1.0";

    private static final String USERNAME = "walter.bates";

    private static final String PWD = "bpm";

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) throws Exception {
        System.setProperty(BONITA_HOME_PROPERTY, BONITA_HOME_LOCATION);
        setupSpringContext();
        try {
            executeApplication();
        } finally {
            closeSpringContext();
        }
        System.out.println("Completed !!!");
    }

    private static void executeApplication() throws BonitaException, InterruptedException {
        PlatformSession platformSession = null;
        try {
            // initialize the platform
            platformSession = doPlatformLogin("platformAdmin", "platform");
            PlatformManager platFormManager = new PlatformManager();
            System.out.println("Initializing the platform ...");
            platFormManager.makePlatformReadyToUsePlatform(platformSession);
            System.out.println("Platform initialized!");

            executeActionsInThePlatform();

            // clean the platform
            System.out.println("Cleaning the platform ...");
            platFormManager.destroyPlaform(platformSession);
            System.out.println("Platform cleaned!");

        } finally {
            doPlatformLogout(platformSession);
        }

    }

    private static void executeActionsInThePlatform() throws BonitaException, InterruptedException {
        APISession tenantSession = null;
        try {
            // create a final user
            System.out.println("Creating user: " + USERNAME);
            tenantSession = doTenantLogin("install", "install");
            OrganizationManager organizationManager = new OrganizationManager();
            User user = organizationManager.createUser(USERNAME, PWD, tenantSession);
            System.out.println("User " + USERNAME + " created!");

            // launch thread that simulates a user waiting for a task to execute
            TaskExecutorThread taskExecutorThread = new TaskExecutorThread(USERNAME, PWD);
            taskExecutorThread.start();

            // deploy the process
            tenantSession = changeLoggedUser(USERNAME, PWD, tenantSession);
            System.out.println("Deploying process ... ");
            ProcessManager processManager = new ProcessManager();
            ProcessDefinition processDefinition = processManager.buildAndDeployProcess(tenantSession);
            System.out.println("Process deployed!");

            // start the process
            System.out.println("Instantiating process ... ");
            ProcessInstance processInstance = processManager.startProcess(processDefinition, tenantSession);
            System.out.println("Process instantiated!");
            
            // at this time the user was able to get a task to execute, so thread can be joined
            taskExecutorThread.join();

            System.out.println("number of executed tasks: " + taskExecutorThread.getNumberOfExecutedTasks());
            if (taskExecutorThread.getException() != null) {
                System.err.println("An exeception occured during task execution: " + taskExecutorThread.getException().getMessage());
            }

            // clean process
            processManager.deleteProcessInstance(processInstance, tenantSession);
            processManager.disableAndDeletProcess(processDefinition, tenantSession);

            // clean users
            tenantSession = changeLoggedUser("install", "install", tenantSession);
            organizationManager.deleteUser(user.getId(), tenantSession);

        } finally {
            doTenantlogout(tenantSession);
        }
    }

    private static void doPlatformLogout(PlatformSession platformSession) throws PlatformLogoutException, SessionNotFoundException, BonitaHomeNotSetException,
            ServerAPIException, UnknownAPITypeException {
        if (platformSession != null) {
            getPlaformLoginAPI().logout(platformSession);
        }

    }

    private static PlatformSession doPlatformLogin(String platformUsername, String password) throws BonitaException {
        return getPlaformLoginAPI().login(platformUsername, password);
    }

    private static PlatformLoginAPI getPlaformLoginAPI() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return PlatformAPIAccessor.getPlatformLoginAPI();
    }

    private static APISession doTenantLogin(String username, String password) throws BonitaException {
        return getLoginAPI().login(username, password);
    }

    private static LoginAPI getLoginAPI() throws BonitaException {
        return TenantAPIAccessor.getLoginAPI();
    }

    private static void doTenantlogout(APISession session) throws BonitaException {
        if (session != null) {
            getLoginAPI().logout(session);
        }
    }

    private static APISession changeLoggedUser(String username, String password, APISession oldSession) throws BonitaException {
        doTenantlogout(oldSession);
        return doTenantLogin(username, password);
    }

    private static void setupSpringContext() {
        System.setProperty("sysprop.bonita.db.vendor", "h2");

        // Force these system properties
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.bonitasoft.engine.local.SimpleMemoryContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.bonitasoft.engine.local");

        springContext = new ClassPathXmlApplicationContext("datasource.xml", "jndi-setup.xml");
    }

    private static void closeSpringContext() {
        springContext.close();
    }
}
