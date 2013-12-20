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

import javax.naming.Context;

import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.session.APISession;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import organization.OrganizationManager;

import com.bonitasoft.example.platform.PlatformManager;
import com.bonitasoft.example.process.ProcessManager;
import com.bonitasoft.example.session.TenantSessionProvider;

public class App {

    private static final String TECHNICAL_USER_NAME = "install";

    private static final String TECHNICAL_PASSWORD = "install";

    private static final String USER_NAME = "walter.bates";

    private static final String PWD = "bpm";

    private static ConfigurableApplicationContext springContext;
    
    private static PlatformManager platFormManager = new PlatformManager();
    
    private static TenantSessionProvider sessionProvider = new TenantSessionProvider();
    
    private static OrganizationManager organizationManager = new OrganizationManager();
    
    private static ProcessManager processManager = new ProcessManager();
    

    public static void main(String[] args) throws Exception {
        startDataSource();
        try {
            executeApplication();
        } finally {
            stopDataSource();
        }
        System.out.println("Completed sucessfully!!!");
    }

    private static void executeApplication() throws BonitaException, InterruptedException, IOException {
        platFormManager.login("platformAdmin", "platform");
        try {
            platFormManager.create();
            executeActionsInThePlatform();
            platFormManager.destroy();
        } finally {
            platFormManager.logout();
        }

    }

    private static void executeActionsInThePlatform() throws BonitaException, InterruptedException, IOException {
        // no end users are created during the platform initialization, so only the technical user is available 
        APISession session = sessionProvider.getNewSession(TECHNICAL_USER_NAME, TECHNICAL_PASSWORD);
        try {
            // logged in as technical user you are able to create end user that will be able to deploy process, execute tasks, ...
            User walter = organizationManager.createUser(USER_NAME, PWD, session);

            //Lets log in as Walter
            session = sessionProvider.getNewSession(USER_NAME, PWD);

            // Walter deploy a process
            ProcessDefinition processDefinition = processManager.deployProcess(session);
            
            StringBuilder stb = new StringBuilder("\nChoose the action to be executed:\n");
            stb.append("1 - start a process\n");
            stb.append("2 - execute a task\n");
            stb.append("3 - exit\n");
            stb.append("Choice:");
            String choice = null; 
            do {
                choice = readLine(stb.toString());
                if("1".equals(choice)) {
                    processManager.startProcess(processDefinition, session);
                } else if ("2".equals(choice)){
                    processManager.executeATask(session);
                } else if (!"3".equals(choice)){
                    System.out.println("Invalid choice!");
                }
            } while(!"3".equals(choice));
            

            // Lets clean all informations
            // clean processes
            processManager.cleanProcesses(processDefinition, session);

            //Before deleting users, lets log with technical user
            session = sessionProvider.getNewSession(TECHNICAL_USER_NAME, TECHNICAL_USER_NAME);
            
            // delete users
            organizationManager.deleteUser(walter, session);
        } finally {
            // logout
            sessionProvider.cleanCurrentSession();
        }
    }

    private static String readLine(String message) throws IOException {
        System.out.println(message);
        String choice = null;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        choice=buffer.readLine();
        return choice;
    }

    private static void startDataSource() {
        System.setProperty("sysprop.bonita.db.vendor", "h2");

        // Force these system properties
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.bonitasoft.engine.local.SimpleMemoryContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.bonitasoft.engine.local");

        springContext = new ClassPathXmlApplicationContext("datasource.xml", "jndi-setup.xml");
    }

    private static void stopDataSource() {
        springContext.close();
    }
}
