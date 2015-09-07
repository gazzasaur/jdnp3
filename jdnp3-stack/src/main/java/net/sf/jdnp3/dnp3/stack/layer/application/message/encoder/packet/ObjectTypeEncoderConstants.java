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
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputEventAbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputEventRelativeTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputEventWithoutTimeTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputStaticFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryInputStaticPackedObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.CrobObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.cto.SynchronisedCtoObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;

public class ObjectTypeEncoderConstants {
	@SuppressWarnings("serial")
	public static final List<ObjectTypeEncoder> OBJECT_TYPE_ENCODERS = new ArrayList<ObjectTypeEncoder>() {{
		this.add(new BinaryInputStaticPackedObjectTypeEncoder());
		this.add(new BinaryInputStaticFlagsObjectTypeEncoder());
		
		this.add(new BinaryInputEventWithoutTimeTimeObjectTypeEncoder());
		this.add(new BinaryInputEventAbsoluteTimeObjectTypeEncoder());
		this.add(new BinaryInputEventRelativeTimeObjectTypeEncoder());
		
		this.add(new CrobObjectTypeEncoder());
		
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

		this.add(new SynchronisedCtoObjectTypeEncoder());
	}};
}
