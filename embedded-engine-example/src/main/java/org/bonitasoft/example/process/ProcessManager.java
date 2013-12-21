package org.bonitasoft.example.process;

import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.InvalidProcessDefinitionException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

public class ProcessManager {

    private static final String ACTOR_NAME = "MyActor";

    private static final String START_NAME = "Start";

    private static final String FIRST_USER_STEP_NAME = "My first step";

    private static final String SECOND_USER_STEP_NAME = "My second step";

    private static final String AUTO_STEP_NAME = "My Automatic Step";

    private static final String END_NAME = "End";

    private static int MAX_ELEMENTS = 100;

    public ProcessDefinition deployProcess(APISession session) throws BonitaException {
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
    }

    public ProcessInstance startProcess(ProcessDefinition processDefinition, APISession session) throws BonitaException {
        System.out.println("Instantiating process ... ");
        ProcessInstance processInstance = getProcessAPI(session).startProcess(processDefinition.getId());
        System.out.println("Process instantiated! Id: " + processInstance.getId());
        return processInstance;
    }

    public void deleteArchivedProcessInstance(ProcessDefinition processDefinition, APISession session) throws BonitaException, InterruptedException {
        long nbOfDeletedProcess = 0;
        do {
            nbOfDeletedProcess = getProcessAPI(session).deleteArchivedProcessInstances(processDefinition.getId(), 0, MAX_ELEMENTS);
        } while (nbOfDeletedProcess == MAX_ELEMENTS);
        System.out.println("Deleted archived processs instances.");
    }

    public void deleteOpenProcessInstance(ProcessDefinition processDefinition, APISession session) throws BonitaException, InterruptedException {
        long nbOfDeletedProcess = 0;
        do {
            nbOfDeletedProcess = getProcessAPI(session).deleteProcessInstances(processDefinition.getId(), 0, MAX_ELEMENTS);
        } while (nbOfDeletedProcess == MAX_ELEMENTS);
        System.out.println("Deleted opened processs instances.");
    }

    public void disableAndDeleteProcess(ProcessDefinition processDefinition, APISession session) throws BonitaException {
        getProcessAPI(session).disableAndDeleteProcessDefinition(processDefinition.getId());
        System.out.println("Process disabled.");
    }

    public void undeployProcess(ProcessDefinition processDefinition, APISession session) throws BonitaException, InterruptedException {
        System.out.println("Undeplyoing process...");
        //before deleting a process is necessary to delete all its instances (opened or archived)
        deleteOpenProcessInstance(processDefinition, session);
        deleteArchivedProcessInstance(processDefinition, session);
        
        disableAndDeleteProcess(processDefinition, session);
        System.out.println("Process undeployed!");
    }

    /**
     * Build a simple process: Start -> My Automatic Step -> My first step -> My second step -> End
     * @return the built process
     * @throws InvalidProcessDefinitionException
     */
    private DesignProcessDefinition buildProcessDefinition() throws InvalidProcessDefinitionException {
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

    private ProcessAPI getProcessAPI(APISession session) throws BonitaException {
        return TenantAPIAccessor.getProcessAPI(session);
    }

    public void executeATask(APISession session) throws BonitaException {
        ProcessAPI processAPI = getProcessAPI(session);
        // get the list of pending tasks (limited to one element) for the logged user.
        List<HumanTaskInstance> pendingTasks = processAPI.getPendingHumanTaskInstances(session.getUserId(), 0, 1, ActivityInstanceCriterion.LAST_UPDATE_ASC);
        if (!pendingTasks.isEmpty()) {
            HumanTaskInstance taskToExecute = pendingTasks.get(0);
            // assign the task
            processAPI.assignUserTask(taskToExecute.getId(), session.getUserId());
            System.out.println("Task '" + taskToExecute.getName() + "' of process instance '" + taskToExecute.getRootContainerId() + "' assigned to '"
                    + session.getUserName() + ".");

            // execute the task
            processAPI.executeFlowNode(taskToExecute.getId());
            System.out.println("Task '" + taskToExecute.getName() + "' of process instance '" + taskToExecute.getRootContainerId() + "' executed by '"
                    + session.getUserName() + ".");
        } else {
            System.out.println("No pending tasks for user '" + session.getUserName() + "'.");
        }
    }

}
