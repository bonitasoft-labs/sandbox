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

import java.text.NumberFormat;
import java.util.concurrent.Callable;

import org.bonitasoft.evaluation.executor.FormatStringExecutor;
import org.bonitasoft.evaluation.executor.ReturnNullWhenNotFoundExecutor;
import org.bonitasoft.evaluation.executor.StringBuilderExecutor;
import org.bonitasoft.evaluation.executor.ThrowExceptionWhenNotFoundExecutor;
import org.junit.Test;

public class MeasureExecutionTimeTest {

	private static final int TIMES = 1000000; 
	
	

	@Test
	public void testWithExceptionThrowing() {
		NumberFormat formatter = NumberFormat.getInstance();	
		Callable<Void> withExceptionExecutor = new ThrowExceptionWhenNotFoundExecutor();
		long timeWithException = new MeasureExecutionTime(withExceptionExecutor).measureFor(TIMES);
		System.out.println("With Exception Throwing execution took " + formatter.format(timeWithException) + " ms.");
		
		Callable<Void> returningNullExecutor = new ReturnNullWhenNotFoundExecutor();
		long timeReturningNull = new MeasureExecutionTime(returningNullExecutor).measureFor(TIMES);
		System.out.println("With Null checking Execution took " + formatter.format(timeReturningNull) + " ms.");
		assert(timeWithException >= timeReturningNull);
	}

	@Test
	public void testStringFormatting() {
		NumberFormat formatter = NumberFormat.getInstance();	
		Callable<String> stringFormatExecutor = new FormatStringExecutor();
		long timeWithStringFormat = new MeasureExecutionTime(stringFormatExecutor).measureFor(TIMES);
		System.out.println("With String Format Execution took " + formatter.format(timeWithStringFormat) + " ms.");
		
		Callable<String> stringBuilder = new StringBuilderExecutor();
		long timeWithStringBuilder = new MeasureExecutionTime(stringBuilder).measureFor(TIMES);
		System.out.println("With StringBuilder Execution took " + formatter.format(timeWithStringBuilder) + " ms.");
		assert(timeWithStringFormat >= timeWithStringBuilder);
	}


}
