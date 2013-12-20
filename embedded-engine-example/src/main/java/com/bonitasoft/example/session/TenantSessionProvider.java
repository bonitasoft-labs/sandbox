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
package com.bonitasoft.example.session;

import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;


public class TenantSessionProvider {
    
    private APISession session = null;

    public APISession getNewSession(String username, String password) throws BonitaException {
        cleanCurrentSession();
        session = getLoginAPI().login(username, password);
        System.out.println("User '" + session.getUserName() + "' has logged in!");
        return session;
    }

    private LoginAPI getLoginAPI() throws BonitaException {
        return TenantAPIAccessor.getLoginAPI();
    }

    public void cleanCurrentSession() throws BonitaException {
        if (session != null) {
            getLoginAPI().logout(session);
            System.out.println("User '" + session.getUserName() + "' has logged out!");
        }
    }
}
