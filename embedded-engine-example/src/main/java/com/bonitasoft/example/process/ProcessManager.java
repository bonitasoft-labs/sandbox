package com.bonitasoft.example.process;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
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

    private static final String USER_STEP_NAME = "My User Step";

    private static final String AUTO_STEP_NAME = "My Automatic Step";

    private static final String END_NAME = "End";

    public ProcessDefinition buildAndDeployProcess(APISession session) throws BonitaException {
            DesignProcessDefinition designProcessDefinition = buildProcessDefinition();
            ProcessDefinition processDefinition = getProcessAPI(session).deploy(designProcessDefinition);
            getProcessAPI(session).addUserToActor(ACTOR_NAME, processDefinition, session.getUserId());
            getProcessAPI(session).enableProcess(processDefinition.getId());
            return processDefinition;
    }

    public ProcessInstance startProcess(ProcessDefinition processDefinition, APISession session) throws BonitaException {
            return getProcessAPI(session).startProcess(processDefinition.getId());
    }

    public void deleteProcessInstance(ProcessInstance processInstance, APISession session) throws BonitaException {
            getProcessAPI(session).deleteProcessInstance(processInstance.getId());
    }

    public void disableAndDeletProcess(ProcessDefinition processDefinition, APISession session) throws BonitaException {
        getProcessAPI(session).disableAndDeleteProcessDefinition(processDefinition.getId());
    }

    private DesignProcessDefinition buildProcessDefinition() throws InvalidProcessDefinitionException {
        ProcessDefinitionBuilder pdb = new ProcessDefinitionBuilder().createNewInstance("My first process", "1.0");
        pdb.addActor(ACTOR_NAME, true);
        pdb.addStartEvent(START_NAME);
        pdb.addAutomaticTask(AUTO_STEP_NAME);
        pdb.addUserTask(USER_STEP_NAME, ACTOR_NAME);
        pdb.addEndEvent(END_NAME);
        pdb.addTransition(START_NAME, AUTO_STEP_NAME);
        pdb.addTransition(AUTO_STEP_NAME, USER_STEP_NAME);
        pdb.addTransition(USER_STEP_NAME, END_NAME);

        return pdb.done();
    }
    
    ProcessAPI getProcessAPI(APISession session) throws BonitaException {
        return TenantAPIAccessor.getProcessAPI(session);
    }

}
