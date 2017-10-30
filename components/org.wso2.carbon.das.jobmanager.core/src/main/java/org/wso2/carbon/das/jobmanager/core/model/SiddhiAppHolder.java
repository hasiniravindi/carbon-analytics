/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.das.jobmanager.core.model;

import java.io.Serializable;

/**
 * Represents a deployed child Siddhi app
 */
public class SiddhiAppHolder implements Serializable {
    private static final long serialVersionUID = 3345845929151967554L;
    private String parentAppName;
    private String groupName;
    private String appName;
    private String siddhiApp;
    private ResourceNode deployedNode;

    private SiddhiAppHolder() {
        // Avoiding empty initialization
    }

    public SiddhiAppHolder(String parentAppName, String groupName, String appName, String siddhiApp,
                           ResourceNode deployedNode) {
        this.parentAppName = parentAppName;
        this.groupName = groupName;
        this.appName = appName;
        this.siddhiApp = siddhiApp;
        this.deployedNode = deployedNode;
    }

    public String getParentAppName() {
        return parentAppName;
    }

    public void setParentAppName(String parentAppName) {
        this.parentAppName = parentAppName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSiddhiApp() {
        return siddhiApp;
    }

    public void setSiddhiApp(String siddhiApp) {
        this.siddhiApp = siddhiApp;
    }

    public ResourceNode getDeployedNode() {
        return deployedNode;
    }

    public void setDeployedNode(ResourceNode deployedNode) {
        this.deployedNode = deployedNode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
