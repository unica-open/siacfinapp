<#--
/*
 * $Id: Action.java 502296 2007-02-01 17:33:39Z niallp $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *se il campo ha un nome composto cioè è associato ad un visitor validator, allora ricerca l'errore per id del campo, altrimenti per nome del campo*
 */
-->
<#--
    Only show message if errors are available.
    This will be done if ActionSupport is used.
-->
<#assign hasFieldErrors = parameters.name?exists && fieldErrors?exists && fieldErrors[parameters.name]?exists/>
<#assign hasFieldErrorsId = parameters.id?exists && fieldErrors?exists && fieldErrors[parameters.id]?exists/>
<#if hasFieldErrors><#t/>
    <#list fieldErrors[parameters.name] as error><#t/>
        <img src="<@s.url value='/struts/${parameters.theme}/error.gif' includeParams='none' encode='false'  />" alt="Errore: ${error?html}" title="Errore: ${error?html}" /><#rt/>
    </#list><#t/>
<#else><#t/>
    <#if hasFieldErrorsId><#t/>
        <#list fieldErrors[parameters.id] as error><#t/>
            <img src="<@s.url value='/struts/${parameters.theme}/error.gif' includeParams='none' encode='false'  />" alt="Errore: ${error?html}" title="Errore: ${error?html}" /><#rt/>
        </#list><#t/>
    </#if><#t/>
</#if><#t/>