/**
 * Copyright (C) 2011-2014 BonitaSoft S.A.
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
package org.bonitasoft.evaluation;

import java.util.concurrent.Callable;

/**
 * @author Nicolas Chabanoles
 * 
 */
public class MeasureExecutionTime {

	private Callable<? extends Object> executor;

	public MeasureExecutionTime(Callable<? extends Object> executor) {
		this.executor = executor;
	}

	public long measureFor(long times) {
		long start = System.currentTimeMillis();
		try {
			for (int i = 0; i < times; i++) {
					executor.call();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return System.currentTimeMillis() - start;

	}
}
