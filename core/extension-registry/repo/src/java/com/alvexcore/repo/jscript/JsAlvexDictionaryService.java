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

package com.alvexcore.repo.jscript;

import com.alvexcore.repo.AlvexDictionaryService;

import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.springframework.beans.factory.annotation.Required;

import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.dictionary.TypeDefinition;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;

import org.alfresco.repo.jscript.ScriptNode;
import org.alfresco.service.cmr.repository.NodeRef;

import org.alfresco.repo.jscript.ValueConverter;
import org.mozilla.javascript.Scriptable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.alfresco.repo.jscript.ScriptableHashMap;
import java.util.Map;
import org.alfresco.service.namespace.QName;

public class JsAlvexDictionaryService extends BaseScopableProcessorExtension {
	
	private DictionaryService dictionaryService;
	private AlvexDictionaryService alvexDictionaryService;
	private ValueConverter converter = new ValueConverter();
	
	@Required
	public void setAlvexDictionaryService(AlvexDictionaryService alvexDictionaryService)
	{
		this.alvexDictionaryService = alvexDictionaryService;
		this.dictionaryService = alvexDictionaryService.getDictionaryService();
	}
	
	public Scriptable getParentHierarchy(String shortName)
	{
		TypeDefinition typeDef = alvexDictionaryService.getDataType(shortName);
		List<TypeDefinition> parents =  alvexDictionaryService.getParentHierarchy(typeDef);
		ArrayList<Serializable> jsRes = new ArrayList<Serializable>();
		for (TypeDefinition type : parents)
		{
			jsRes.add( type.getName().getPrefixString() );
		}
		
		return (Scriptable)converter.convertValueForScript(
								alvexDictionaryService.getServiceRegistry(), 
								getScope(), null, jsRes);
	}
	
	public boolean isContent(ScriptNode node)
	{
		return alvexDictionaryService.isContent(node.getNodeRef());
	}
	
	public boolean isRegistry(ScriptNode node)
	{
		return alvexDictionaryService.isRegistry(node.getNodeRef());
	}
	
	public boolean isRegistryItem(ScriptNode node)
	{
		return alvexDictionaryService.isRegistryItem(node.getNodeRef());
	}
	
	public Scriptable getCompleteTypeDescription(String shortName)
	{
		ScriptableHashMap<String, Object> jsType = new ScriptableHashMap<String, Object>();
		ArrayList<ScriptableHashMap<String,String>> fields = new ArrayList<ScriptableHashMap<String,String>>();
		
		TypeDefinition typeDef = alvexDictionaryService.getDataType(shortName);
		Map<QName, PropertyDefinition> typeDesc = alvexDictionaryService.getCompleteTypeDescription(typeDef);
		
		jsType.put("title", typeDef.getTitle(dictionaryService));
		jsType.put("type", typeDef.getName().getPrefixString());
		for (Map.Entry<QName, PropertyDefinition> entry : typeDesc.entrySet())
		{
			PropertyDefinition def = entry.getValue();
			ScriptableHashMap<String, String> pr = new ScriptableHashMap<String, String>();
			pr.put("type", def.getName().getPrefixString());
			pr.put("title", def.getTitle(dictionaryService));
			fields.add(pr);
		}
		jsType.put("fields", fields);
		
		return (Scriptable)converter.convertValueForScript(
								alvexDictionaryService.getServiceRegistry(), 
								getScope(), null, jsType);
	}
}