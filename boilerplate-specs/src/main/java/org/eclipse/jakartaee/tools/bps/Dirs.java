/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.eclipse.jakartaee.tools.bps;

import org.tomitribe.util.JarLocation;
import org.tomitribe.util.dir.Dir;
import org.tomitribe.util.dir.Name;

import java.io.File;

public class Dirs {

    public static Work work() {
        final File targetClasses = JarLocation.jarLocation(Dirs.class);
        final File moduleDir = targetClasses.getParentFile().getParentFile();
        final File projectDir = moduleDir.getParentFile();
        return org.tomitribe.util.dir.Dir.of(Work.class, new File(projectDir, "work"));
    }

    interface Work extends Dir {
        File repos();

        File forks();
    }

}