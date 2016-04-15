/*
 * Copyright 2013 undera.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atlantbh.jmeter.plugins.hbasecomponents.config;

import com.atlantbh.jmeter.plugins.hbasecrud.HTableEmul;
import java.io.IOException;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

class HBaseConnectionVariableEmul extends HBaseConnectionVariable {

    public HBaseConnectionVariableEmul() {
        tablePool = new HTablePool();
    }

    @Override
    public HTableInterface getTable(String tableName) throws IOException {
        return new HTableEmul();
    }

    @Override
    public void putTable(HTableInterface table) {
    }
}
