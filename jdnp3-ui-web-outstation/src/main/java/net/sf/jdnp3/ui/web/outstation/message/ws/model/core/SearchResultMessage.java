/**
 * Copyright 2024 Graeme Farquharson
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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.core;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.ui.web.outstation.main.SearchResultItem;

public class SearchResultMessage implements Message {
	private String type = "searchResult";
	private String searchTerm = "";
	private List<SearchResultItem> results = new ArrayList<SearchResultItem>();

	public String getType() {
		return type;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public List<SearchResultItem> getResults() {
		return results;
	}

	public void setResults(List<SearchResultItem> results) {
		this.results = results;
	}
}
