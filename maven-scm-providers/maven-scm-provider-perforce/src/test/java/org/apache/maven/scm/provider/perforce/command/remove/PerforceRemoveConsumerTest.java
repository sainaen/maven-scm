package org.apache.maven.scm.provider.perforce.command.remove;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmTestCase;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 *
 */
public class PerforceRemoveConsumerTest
    extends ScmTestCase
{
    public void testParse()
        throws Exception
    {
        File testFile = getTestFile( "src/test/resources/perforce/removelog.txt" );

        PerforceRemoveConsumer consumer = new PerforceRemoveConsumer();

        FileInputStream fis = new FileInputStream( testFile );
        BufferedReader in = new BufferedReader( new InputStreamReader( fis ) );
        String s = in.readLine();
        while ( s != null )
        {
            consumer.consumeLine( s );
            s = in.readLine();
        }

        List<ScmFile> removes = consumer.getRemovals();
        assertEquals( "Wrong number of entries returned", 2, removes.size() );
        String entry = removes.get( 0 ).getPath();
        assertTrue( entry.startsWith( "//" ) );
        assertTrue( entry.endsWith( "foo.xml" ) );
    }
}
