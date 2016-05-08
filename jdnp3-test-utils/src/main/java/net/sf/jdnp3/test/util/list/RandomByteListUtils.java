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
package net.sf.jdnp3.test.util.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

public class RandomByteListUtils {
	public static List<Byte> create() {
		List<Byte> data = new ArrayList<Byte>();
		randomise(data);
		return data;
	}
	
	public static void randomise(List<Byte> data) {
		List<Byte> original = new ArrayList<>(data);
		int changeCount = RandomUtils.nextInt(0, 1000);
		for (int i = 0; i < changeCount || original.equals(data); ++i) {
			double chance = RandomUtils.nextDouble(0, 1);
			int index = RandomUtils.nextInt(0, (data.size() - 1 >= 0) ? data.size() : 0);
			if (chance < 0.1 && data.size() > 0) {
				data.remove(index);
			} else if (chance < 0.8 && data.size() > 0) {
				data.set(index, RandomUtils.nextBytes(1)[0]);
			} else {
				data.add(RandomUtils.nextBytes(1)[0]);
			}
		}
	}
}
