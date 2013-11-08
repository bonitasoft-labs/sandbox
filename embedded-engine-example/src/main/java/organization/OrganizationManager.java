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
package organization;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.session.APISession;

public class OrganizationManager {



    public User createUser(String username, String password, APISession session) throws BonitaException {
        return getIdentityAPI(session).createUser(username, password);
    }

    private IdentityAPI getIdentityAPI(APISession session) throws BonitaException {
        return TenantAPIAccessor.getIdentityAPI(session);
    }

    public void deleteUser(long userId, APISession session) throws BonitaException {
            getIdentityAPI(session).deleteUser(userId);;
    }

}
