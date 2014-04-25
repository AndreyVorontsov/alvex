/*
 * Copyright © 2014 ITD Systems
 *
 * This file is part of Alvex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alvexcore.repo.registries;

import java.util.List;
import java.util.Map;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;

public interface AlvexRegistriesService {

	public abstract Map<String,String> getParentRegistryDetails(NodeRef recordRef);
	public abstract String suggestNextNumber(NodeRef registryRef);
	public abstract boolean commitNextNumber(NodeRef registryRef, String number, PropertyDefinition prop);
	public abstract List<Map<String,String>> getParentRegistryItems(NodeRef fileRef);
	public abstract boolean workflowsAvailableForRegistryItem();
	
	public abstract ServiceRegistry getServiceRegistry();
}