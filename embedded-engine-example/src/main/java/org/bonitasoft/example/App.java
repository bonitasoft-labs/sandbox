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
package org.bonitasoft.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProcessRuntimeAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.InvalidProcessDefinitionException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.PlatformSession;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    private static final String PLATFORM_PASSWORD = "platform";

    private static final String PLATFORM_ADMIN = "platformAdmin";

    private static final String TECHNICAL_USER_NAME = "install";

    private static final String TECHNICAL_PASSWORD = "install";

    private static final String USER_NAME = "walter.bates";

    private static final String PWD = "bpm";
    
    private static final String ACTOR_NAME = "MyActor";

    private static final String START_NAME = "Start";

    private static final String FIRST_USER_STEP_NAME = "My first step";

    private static final String SECOND_USER_STEP_NAME = "My second step";

    private static final String AUTO_STEP_NAME = "My Automatic Step";

    private static final String END_NAME = "End";

    private static int MAX_ELEMENTS = 100;

    private static ConfigurableApplicationContext springContext;

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
            System.out.println("Deploying process ... ");
            DesignProcessDefinition designProcessDefinition = buildProcessDefinition();
            ProcessDefinition processDefinition = getProcessAPI(session).deploy(designProcessDefinition);
            System.out.println("Process deployed!");
            
            System.out.println("Mapping actors ... ");
            getProcessAPI(session).addUserToActor(ACTOR_NAME, processDefinition, session.getUserId());
            System.out.println("Actors mapped!");
            
            System.out.println("Enabling process ... ");
            getProcessAPI(session).enableProcess(processDefinition.getId());
            System.out.println("Process enabled!");
            return processDefinition;

        } finally {
            doTenantLogout(session);
        }
    }
    
    /**
     * Build a simple process: Start -> My Automatic Step -> My first step -> My second step -> End
     * @return the built process
     * @throws InvalidProcessDefinitionException
     */
    private static DesignProcessDefinition buildProcessDefinition() throws InvalidProcessDefinitionException {
        ProcessDefinitionBuilder pdb = new ProcessDefinitionBuilder().createNewInstance("My first process", "1.0");
        pdb.addActor(ACTOR_NAME, true);
        pdb.addStartEvent(START_NAME);
        pdb.addAutomaticTask(AUTO_STEP_NAME);
        pdb.addUserTask(FIRST_USER_STEP_NAME, ACTOR_NAME);
        pdb.addUserTask(SECOND_USER_STEP_NAME, ACTOR_NAME);
        pdb.addEndEvent(END_NAME);
        pdb.addTransition(START_NAME, AUTO_STEP_NAME);
        pdb.addTransition(AUTO_STEP_NAME, FIRST_USER_STEP_NAME);
        pdb.addTransition(FIRST_USER_STEP_NAME, SECOND_USER_STEP_NAME);
        pdb.addTransition(SECOND_USER_STEP_NAME, END_NAME);

        return pdb.done();
    }

    private static void instantiateProcessesAndExecuteTasks(ProcessDefinition processDefinition) throws IOException, BonitaException {
        String message = getMenutTextContent();
        String choice = null;
        do {
            choice = readLine(message);
            if ("1".equals(choice)) {
                startProcess(processDefinition);
            } else if ("2".equals(choice)) {
                executeATask();
            } else if (!"3".equals(choice)) {
                System.out.println("Invalid choice!");
            }
        } while (!"3".equals(choice));
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

    private static void startProcess(ProcessDefinition processDefinition) throws BonitaException {
        APISession session = doTenantLogin(USER_NAME, PWD);
        try {
            System.out.println("Instantiating process ... ");
            ProcessInstance processInstance = getProcessAPI(session).startProcess(processDefinition.getId());
            System.out.println("Process instantiated! Id: " + processInstance.getId());
        } finally {
            doTenantLogout(session);
        }
    }

    private static void executeATask() throws BonitaException {
        APISession session = doTenantLogin(USER_NAME, PWD);
        try {
            ProcessAPI processAPI = getProcessAPI(session);
            // get the list of pending tasks (limited to one element) for the logged user.
            List<HumanTaskInstance> pendingTasks = processAPI.getPendingHumanTaskInstances(session.getUserId(), 0, 1, ActivityInstanceCriterion.LAST_UPDATE_ASC);
            if (!pendingTasks.isEmpty()) {
                executeTask(session, pendingTasks.get(0));
            } else {
                System.out.println("No pending tasks for user '" + session.getUserName() + "'.");
            }
        } finally {
            doTenantLogout(session);
        }
    }

    private static void executeTask(APISession session, HumanTaskInstance taskToExecute) throws BonitaException {
        ProcessRuntimeAPI processAPI = getProcessAPI(session);
        // assign the task
        processAPI .assignUserTask(taskToExecute.getId(), session.getUserId());
        System.out.println("Task '" + taskToExecute.getName() + "' of process instance '" + taskToExecute.getRootContainerId() + "' assigned to '"
                + session.getUserName() + ".");
         
        // execute the task
        processAPI.executeFlowNode(taskToExecute.getId());
        System.out.println("Task '" + taskToExecute.getName() + "' of process instance '" + taskToExecute.getRootContainerId() + "' executed by '"
                + session.getUserName() + ".");
    }

    private static void undeployProcess(ProcessDefinition processDefinition) throws BonitaException, InterruptedException {
        APISession session = doTenantLogin(USER_NAME, PWD);
        try {
            System.out.println("Undeplyoing process...");
            //before deleting a process is necessary to delete all its instances (opened or archived)
            deleteOpenProcessInstance(processDefinition, session);
            deleteArchivedProcessInstance(processDefinition, session);
            
            disableAndDeleteProcess(processDefinition, session);
            System.out.println("Process undeployed!");
        } finally {
            doTenantLogout(session);
        }
    }
    
    public static void deleteArchivedProcessInstance(ProcessDefinition processDefinition, APISession session) throws BonitaException, InterruptedException {
        //delete archived process instances by block of MAX_ELEMENTS
        long nbOfDeletedProcess = 0;
        do {
            nbOfDeletedProcess = getProcessAPI(session).deleteArchivedProcessInstances(processDefinition.getId(), 0, MAX_ELEMENTS);
        } while (nbOfDeletedProcess == MAX_ELEMENTS);
        System.out.println("Deleted archived processs instances.");
    }

    public static void deleteOpenProcessInstance(ProcessDefinition processDefinition, APISession session) throws BonitaException, InterruptedException {
        //delete opened process instances by block of MAX_ELEMENTS
        long nbOfDeletedProcess = 0;
        do {
            nbOfDeletedProcess = getProcessAPI(session).deleteProcessInstances(processDefinition.getId(), 0, MAX_ELEMENTS);
        } while (nbOfDeletedProcess == MAX_ELEMENTS);
        System.out.println("Deleted opened processs instances.");
    }

    public static void disableAndDeleteProcess(ProcessDefinition processDefinition, APISession session) throws BonitaException {
        getProcessAPI(session).disableAndDeleteProcessDefinition(processDefinition.getId());
        System.out.println("Process disabled.");
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

    public static PlatformSession doPlatformLogin(String platformUsername, String password) throws BonitaException {
        return getPlaformLoginAPI().login(platformUsername, password);
    }

    public static void doPlatformLogout(PlatformSession session) throws BonitaException {
        getPlaformLoginAPI().logout(session);
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
    
    private static ProcessAPI getProcessAPI(APISession session) throws BonitaException {
        return TenantAPIAccessor.getProcessAPI(session);
    }

    private static void deployDataSource() {
        springContext = new ClassPathXmlApplicationContext("engine.cfg.xml");
    }

    private static void undeployDataSource() {
        springContext.close();
    }

}
