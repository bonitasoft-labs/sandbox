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
package com.bonitasoft.example.platform;

import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.PlatformSession;

public class PlatformManager {


    private PlatformSession session = null;

    public PlatformManager() {
    }

    public void create() throws BonitaException {
        System.out.println("Creating and initializing the platform ...");
        getPlatformAPI(session).createAndInitializePlatform();
        System.out.println("Platform created and initialized!");

        System.out.println("Starting node ...");
        getPlatformAPI(session).startNode();
        System.out.println("Node started!");
    }

    private PlatformAPI getPlatformAPI(PlatformSession platformSession) throws BonitaException {
        return PlatformAPIAccessor.getPlatformAPI(platformSession);
    }

    public void destroy() throws BonitaException {
        System.out.println("Stopping node ...");
        getPlatformAPI(session).stopNode();
        System.out.println("Node stopped!");
        
        System.out.println("Cleaning and deleting the platform ...");
        getPlatformAPI(session).cleanAndDeletePlaftorm();;
        System.out.println("Platform cleaned and deleted!");
    }

    public void login(String platformUsername, String password) throws BonitaException {
        session = getPlaformLoginAPI().login(platformUsername, password);
    }

    public void logout() throws BonitaException {
        if (session != null) {
            getPlaformLoginAPI().logout(session);
        }
    }

    private PlatformLoginAPI getPlaformLoginAPI() throws BonitaException {
        return PlatformAPIAccessor.getPlatformLoginAPI();
    }

}
