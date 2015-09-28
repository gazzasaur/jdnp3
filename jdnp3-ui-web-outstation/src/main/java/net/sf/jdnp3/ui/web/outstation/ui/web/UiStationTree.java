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
package net.sf.jdnp3.ui.web.outstation.ui.web;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class UiStationTree {
    private TreeNode root;
    
    @PostConstruct
    public void init() {
    	
        root = new DefaultTreeNode("Root", null);
        
        List<String> stationNames = DatabaseManagerProvider.getStationNames();
        Collections.sort(stationNames);
        
        for (String station : stationNames) {
        	List<String> deviceNames = DatabaseManagerProvider.getDeviceNames(station);
        	Collections.sort(deviceNames);
        	
            TreeNode stationNode = new DefaultTreeNode(station, root);
			for (String device : deviceNames) {
				UiDeviceNode node = new UiDeviceNode();
				node.setStation(station);
				node.setDevice(device);
				
				TreeNode deviceNode = new DefaultTreeNode("leaf", node, stationNode);
				stationNode.getChildren().add(deviceNode);
			}
			root.getChildren().add(stationNode);
		}
    }
 
    public TreeNode getRoot() {
        return root;
    }
}