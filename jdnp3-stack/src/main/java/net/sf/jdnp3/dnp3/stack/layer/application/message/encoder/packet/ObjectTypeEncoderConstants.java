/**
 * Copyright 2016 Graeme Farquharson
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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventFloat32AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventFloat32NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventFloat64AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventFloat64NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventInteger16AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventInteger16NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventInteger32AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputEventInteger32NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputStaticFloat32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputStaticFloat64ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputStaticInteger16NoFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputStaticInteger16ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputStaticInteger32NoFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogInputStaticInteger32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputCommandFloat32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputCommandFloat64ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputCommandInteger16ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputCommandInteger32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventFloat32AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventFloat32NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventFloat64AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventFloat64NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventInteger16AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventInteger16NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventInteger32AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputEventInteger32NoTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputStaticFloat32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputStaticFloat64ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputStaticInteger16ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.analog.AnalogOutputStaticInteger32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputEventAbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputEventRelativeTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputEventWithoutTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputStaticFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputStaticPackedObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryOutputEventAbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryOutputEventWithoutTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryOutputStaticFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryOutputStaticPackedObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.CrobObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger16AbsoluteTimeDeltaObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger16AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger16DeltaObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger16ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger32AbsoluteTimeDeltaObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger32AbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger32DeltaObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterEventInteger32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger16DeltaNoFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger16DeltaObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger16NoFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger16ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger32DeltaNoFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger32DeltaObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger32NoFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.counter.CounterInteger32ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.time.SynchronisedCtoObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.time.TimeAndDateObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.time.TimeDelayFineObjectTypeEncoder;

public class ObjectTypeEncoderConstants {
	@SuppressWarnings("serial")
	public static final List<ObjectTypeEncoder> OBJECT_TYPE_ENCODERS = new ArrayList<ObjectTypeEncoder>() {{
		this.add(new BinaryInputStaticPackedObjectTypeEncoder());
		this.add(new BinaryInputStaticFlagsObjectTypeEncoder());
		
		this.add(new BinaryInputEventWithoutTimeObjectTypeEncoder());
		this.add(new BinaryInputEventAbsoluteTimeObjectTypeEncoder());
		this.add(new BinaryInputEventRelativeTimeObjectTypeEncoder());
		
		this.add(new BinaryOutputStaticPackedObjectTypeEncoder());
		this.add(new BinaryOutputStaticFlagsObjectTypeEncoder());
		
		this.add(new BinaryOutputEventWithoutTimeObjectTypeEncoder());
		this.add(new BinaryOutputEventAbsoluteTimeObjectTypeEncoder());
		
		this.add(new CrobObjectTypeEncoder());

		this.add(new CounterInteger32ObjectTypeEncoder());
		this.add(new CounterInteger16ObjectTypeEncoder());
		this.add(new CounterInteger32DeltaObjectTypeEncoder());
		this.add(new CounterInteger16DeltaObjectTypeEncoder());
		this.add(new CounterInteger32NoFlagsObjectTypeEncoder());
		this.add(new CounterInteger16NoFlagsObjectTypeEncoder());
		this.add(new CounterInteger32DeltaNoFlagsObjectTypeEncoder());
		this.add(new CounterInteger16DeltaNoFlagsObjectTypeEncoder());
		
		this.add(new CounterEventInteger16ObjectTypeEncoder());
		this.add(new CounterEventInteger32ObjectTypeEncoder());
		this.add(new CounterEventInteger16DeltaObjectTypeEncoder());
		this.add(new CounterEventInteger32DeltaObjectTypeEncoder());
		this.add(new CounterEventInteger16AbsoluteTimeObjectTypeEncoder());
		this.add(new CounterEventInteger32AbsoluteTimeObjectTypeEncoder());
		this.add(new CounterEventInteger16AbsoluteTimeDeltaObjectTypeEncoder());
		this.add(new CounterEventInteger32AbsoluteTimeDeltaObjectTypeEncoder());

		this.add(new AnalogInputStaticInteger32ObjectTypeEncoder());
		this.add(new AnalogInputStaticInteger16ObjectTypeEncoder());
		this.add(new AnalogInputStaticInteger32NoFlagsObjectTypeEncoder());
		this.add(new AnalogInputStaticInteger16NoFlagsObjectTypeEncoder());
		this.add(new AnalogInputStaticFloat32ObjectTypeEncoder());
		this.add(new AnalogInputStaticFloat64ObjectTypeEncoder());

		this.add(new AnalogInputEventInteger32NoTimeObjectTypeEncoder());
		this.add(new AnalogInputEventInteger16NoTimeObjectTypeEncoder());
		this.add(new AnalogInputEventInteger32AbsoluteTimeObjectTypeEncoder());
		this.add(new AnalogInputEventInteger16AbsoluteTimeObjectTypeEncoder());
		this.add(new AnalogInputEventFloat32NoTimeObjectTypeEncoder());
		this.add(new AnalogInputEventFloat64NoTimeObjectTypeEncoder());
		this.add(new AnalogInputEventFloat32AbsoluteTimeObjectTypeEncoder());
		this.add(new AnalogInputEventFloat64AbsoluteTimeObjectTypeEncoder());

		this.add(new AnalogOutputStaticInteger32ObjectTypeEncoder());
		this.add(new AnalogOutputStaticInteger16ObjectTypeEncoder());
		this.add(new AnalogOutputStaticFloat32ObjectTypeEncoder());
		this.add(new AnalogOutputStaticFloat64ObjectTypeEncoder());
		
		this.add(new AnalogOutputEventInteger32NoTimeObjectTypeEncoder());
		this.add(new AnalogOutputEventInteger16NoTimeObjectTypeEncoder());
		this.add(new AnalogOutputEventInteger32AbsoluteTimeObjectTypeEncoder());
		this.add(new AnalogOutputEventInteger16AbsoluteTimeObjectTypeEncoder());
		this.add(new AnalogOutputEventFloat32NoTimeObjectTypeEncoder());
		this.add(new AnalogOutputEventFloat64NoTimeObjectTypeEncoder());
		this.add(new AnalogOutputEventFloat32AbsoluteTimeObjectTypeEncoder());
		this.add(new AnalogOutputEventFloat64AbsoluteTimeObjectTypeEncoder());

		this.add(new AnalogOutputCommandInteger32ObjectTypeEncoder());
		this.add(new AnalogOutputCommandInteger16ObjectTypeEncoder());
		this.add(new AnalogOutputCommandFloat32ObjectTypeEncoder());
		this.add(new AnalogOutputCommandFloat64ObjectTypeEncoder());

		this.add(new SynchronisedCtoObjectTypeEncoder());
		this.add(new TimeDelayFineObjectTypeEncoder());
		
		this.add(new TimeAndDateObjectTypeEncoder());
	}};
}
