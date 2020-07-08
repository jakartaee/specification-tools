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
package org.eclipse.jakartaee.tools.reviews;

import java.util.Iterator;

public class InfiniteIterable<T> implements Iterable<T> {

    private final Iterable<T> source;

    public InfiniteIterable(final Iterable<T> source) {
        this.source = source;
    }

    @Override
    public Iterator<T> iterator() {
        return new InfiniteIterator();
    }

    private class InfiniteIterator implements Iterator<T> {

        private Iterator<T> current;

        public InfiniteIterator() {
            current = source.iterator();
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public T next() {
            if (!current.hasNext()) {

                // Refresh our iterator and effectively loop back to the top
                current = source.iterator();

                if (!current.hasNext()) {
                    throw new IllegalStateException("Source Iterable contains no elements");
                }
            }
            
            return current.next();
        }
    }
}
