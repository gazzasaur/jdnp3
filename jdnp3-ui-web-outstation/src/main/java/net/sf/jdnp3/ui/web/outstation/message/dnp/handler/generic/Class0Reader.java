/**
 * Copyright 2015 Graeme Farquharson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.generic.Class0ReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog.AnalogInputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog.AnalogOutputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary.BinaryInputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary.BinaryOutputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.counter.CounterStaticHandler;

public class Class0Reader implements Class0ReadRequestHandler {
	private AnalogInputStaticHandler analogInputStaticReader;
	private BinaryInputStaticHandler binaryInputStaticReader;
	private AnalogOutputStaticHandler analogOutputStaticReader;
	private BinaryOutputStaticHandler binaryOutputStaticReader;
	private CounterStaticHandler counterStaticReader;
	
	public Class0Reader(DatabaseManager databaseManager) {
		analogInputStaticReader = new AnalogInputStaticHandler(databaseManager);
		binaryInputStaticReader = new BinaryInputStaticHandler(databaseManager);
		analogOutputStaticReader = new AnalogOutputStaticHandler(databaseManager);
		binaryOutputStaticReader = new BinaryOutputStaticHandler(databaseManager);
		counterStaticReader = new CounterStaticHandler(databaseManager);
	}
	
	public List<ObjectInstance> doReadClass() {
		List<ObjectInstance> points = new ArrayList<>();
		points.addAll(binaryInputStaticReader.readStatics());
		points.addAll(analogInputStaticReader.readStatics());
		points.addAll(binaryOutputStaticReader.readStatics());
		points.addAll(analogOutputStaticReader.readStatics());
		points.addAll(counterStaticReader.readStatics());

		return points;
	}
}
