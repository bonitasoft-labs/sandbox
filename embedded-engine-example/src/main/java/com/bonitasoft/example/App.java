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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.PlatformSession;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bonitasoft.example.process.ProcessManager;

public class App {

    private static final String PLATFORM_PASSWORD = "platform";

    private static final String PLATFORM_ADMIN = "platformAdmin";

    private static final String TECHNICAL_USER_NAME = "install";

    private static final String TECHNICAL_PASSWORD = "install";

    private static final String USER_NAME = "walter.bates";

    private static final String PWD = "bpm";

    private static ConfigurableApplicationContext springContext;

    private static ProcessManager processManager = new ProcessManager();

    public static void main(String[] args) throws Exception {
        // TODO: generate bonita home automatically at each run
        deployDataSource();
        try {
            createPlatform();
            User user = createUser();
            ProcessDefinition processDefinition = deployProcess();
            instantiateProcessesAndExecuteTasks(processDefinition);
            undeployProcess(processDefinition);
            deleteUser(user);
            detroyPlatform();
        } finally {
            undeployDataSource();
        }
        System.out.println("Completed sucessfully!!!");
    }

    public static PlatformSession doPlatformLogin(String platformUsername, String password) throws BonitaException {
        return getPlaformLoginAPI().login(platformUsername, password);
    }

    public static void doPlatformLogout(PlatformSession session) throws BonitaException {
        getPlaformLoginAPI().logout(session);
    }

    private static void createPlatform() throws BonitaException {
        PlatformSession session = doPlatformLogin(PLATFORM_ADMIN, PLATFORM_PASSWORD);
        try {
            System.out.println("Creating and initializing the platform ...");
            getPlatformAPI(session).createAndInitializePlatform();
            System.out.println("Platform created and initialized!");

            System.out.println("Starting node ...");
            getPlatformAPI(session).startNode();
            System.out.println("Node started!");
        } finally {
            doPlatformLogout(session);
        }
    }

    private static void detroyPlatform() throws BonitaException {
        PlatformSession session = doPlatformLogin(PLATFORM_ADMIN, PLATFORM_PASSWORD);
        try {
            System.out.println("Stopping node ...");
            getPlatformAPI(session).stopNode();
            System.out.println("Node stopped!");

            System.out.println("Cleaning and deleting the platform ...");
            getPlatformAPI(session).cleanAndDeletePlaftorm();;
            System.out.println("Platform cleaned and deleted!");
        } finally {
            doPlatformLogout(session);
        }
    }

    private static ProcessDefinition deployProcess() throws BonitaException {
        // log in with the real user previously created 
        APISession session = doTenantLogin(USER_NAME, PWD);
        try {
            return processManager.deployProcess(session);

        } finally {
            doTenantLogout(session);
        }
    }

    private static void instantiateProcessesAndExecuteTasks(ProcessDefinition processDefinition) throws IOException, BonitaException {
        APISession session = doTenantLogin(USER_NAME, PWD);
        try {
            String message = getMenutTextContent();
            String choice = null;
            do {
                choice = readLine(message);
                if ("1".equals(choice)) {
                    processManager.startProcess(processDefinition, session);
                } else if ("2".equals(choice)) {
                    processManager.executeATask(session);
                } else if (!"3".equals(choice)) {
                    System.out.println("Invalid choice!");
                }
            } while (!"3".equals(choice));
        } finally {
            doTenantLogout(session);
        }
    }

    private static String getMenutTextContent() {
        StringBuilder stb = new StringBuilder("\nChoose the action to be executed:\n");
        stb.append("1 - start a process\n");
        stb.append("2 - execute a task\n");
        stb.append("3 - exit\n");
        stb.append("Choice:");
        String message = stb.toString();
        return message;
    }

    private static void undeployProcess(ProcessDefinition processDefinition) throws BonitaException, InterruptedException {
        APISession session = doTenantLogin(USER_NAME, PWD);
        try {
            processManager.undeployProcess(processDefinition, session);
        } finally {
            doTenantLogout(session);
        }
    }

    private static void deleteUser(User user) throws BonitaException {
        // In order to delete the only real user, lets log in with technical user
        APISession session = doTenantLogin(TECHNICAL_USER_NAME, TECHNICAL_USER_NAME);
        try {
            getIdentityAPI(session).deleteUser(user.getId());;
            System.out.println("Deleted user '" + user.getUserName() + "'.");
        } finally {
            doTenantLogout(session);
        }
    }

    private static User createUser() throws BonitaException {
        // no end users are created during the platform initialization, so only the technical user is available
        // logged in as technical user you are able to create end user that will be able to deploy process, execute tasks, ...
        APISession session = doTenantLogin(TECHNICAL_USER_NAME, TECHNICAL_PASSWORD);
        User user = null;
        try {
            user = getIdentityAPI(session).createUser(USER_NAME, PWD);
            System.out.println("Created user '" + USER_NAME + "'.");
        } finally {
            doTenantLogout(session);
        }
        return user;
    }

    private static String readLine(String message) throws IOException {
        System.out.println(message);
        String choice = null;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        choice = buffer.readLine();
        return choice;
    }

    public static APISession doTenantLogin(String username, String password) throws BonitaException {
        APISession session = getLoginAPI().login(username, password);
        System.out.println("User '" + username + "' has logged in!");
        return session;
    }

    public static void doTenantLogout(APISession session) throws BonitaException {
        getLoginAPI().logout(session);
        System.out.println("User '" + session.getUserName() + "' has logged out!");
    }

    private static LoginAPI getLoginAPI() throws BonitaException {
        return TenantAPIAccessor.getLoginAPI();
    }

    private static PlatformLoginAPI getPlaformLoginAPI() throws BonitaException {
        return PlatformAPIAccessor.getPlatformLoginAPI();
    }

    private static PlatformAPI getPlatformAPI(PlatformSession platformSession) throws BonitaException {
        return PlatformAPIAccessor.getPlatformAPI(platformSession);
    }

    private static IdentityAPI getIdentityAPI(APISession session) throws BonitaException {
        return TenantAPIAccessor.getIdentityAPI(session);
    }

    private static void deployDataSource() {
        springContext = new ClassPathXmlApplicationContext("engine.cfg.xml");
    }

    private static void undeployDataSource() {
        springContext.close();
    }

}
