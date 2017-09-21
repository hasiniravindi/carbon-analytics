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

package org.wso2.carbon.business.rules.core.internal.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.business.rules.core.internal.bean.BusinessRule;
import org.wso2.carbon.business.rules.core.internal.bean.RuleTemplate;
import org.wso2.carbon.business.rules.core.internal.bean.RuleTemplateProperty;
import org.wso2.carbon.business.rules.core.internal.bean.Template;
import org.wso2.carbon.business.rules.core.internal.bean.TemplateGroup;
import org.wso2.carbon.business.rules.core.internal.bean.businessRulesFromScratch.BusinessRuleFromScratch;
import org.wso2.carbon.business.rules.core.internal.bean.businessRulesFromTemplate.BusinessRuleFromTemplate;
import org.wso2.carbon.business.rules.core.internal.exceptions.TemplateManagerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

/**
 * Consists of methods for additional features for the exposed Template Manager service
 */
//TODO : Verify class names
public class TemplateManagerHelper {
    private static final Logger log = LoggerFactory.getLogger(TemplateManagerHelper.class);

    /**
     * To avoid instantiation
     */
    private TemplateManagerHelper() {

    }

    /**
     * Converts given JSON File to a JSON object
     *
     * @param jsonFile
     * @return
     * @throws TemplateManagerException
     */
    public static JsonObject fileToJson(File jsonFile) throws TemplateManagerException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        JsonObject jsonObject = null;

        try {
            Reader reader = new FileReader(jsonFile);
            jsonObject = gson.fromJson(reader, JsonObject.class);
        } catch (FileNotFoundException e) {
            throw new TemplateManagerException("File - " + jsonFile.getName() + " not found", e);
        } catch (Exception e) {
            throw new TemplateManagerException(e);
        }

        return jsonObject;
    }

    /**
     * Converts given JSON object to TemplateGroup object
     *
     * @param jsonObject Given JSON object
     * @return TemplateGroup object
     */
    public static TemplateGroup jsonToTemplateGroup(JsonObject jsonObject) {
        String templateGroupJsonString = jsonObject.get("templateGroup").toString();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        TemplateGroup templateGroup = gson.fromJson(templateGroupJsonString, TemplateGroup.class);

        return templateGroup;
    }

    /**
     * Converts given String JSON definition to TemplateGroup object
     *
     * @param jsonDefinition Given String JSON definition
     * @return TemplateGroup object
     */
    public static TemplateGroup jsonToTemplateGroup(String jsonDefinition) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        TemplateGroup templateGroup = gson.fromJson(jsonDefinition, TemplateGroup.class);

        return templateGroup;
    }

    /**
     * Converts given JSON object to BusinessRuleFromTemplate object
     *
     * @param jsonObject Given JSON object
     * @return BusinessRuleFromTemplate object
     */
    public static BusinessRuleFromTemplate jsonToBusinessRuleFromTemplate(JsonObject jsonObject) {
        String businessRuleJsonString = jsonObject.get("businessRule").toString();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        BusinessRuleFromTemplate businessRuleFromTemplate = gson.fromJson(businessRuleJsonString, BusinessRuleFromTemplate.class);

        return businessRuleFromTemplate;
    }

    /**
     * Converts given JSON object to BusinessRuleFromScratch object
     *
     * @param jsonObject Given JSON object
     * @return BusinessRuleFromTemplate object
     */
    public static BusinessRuleFromScratch jsonToBusinessRuleFromScratch(JsonObject jsonObject) {
        String businessRuleJsonString = jsonObject.get("businessRule").toString();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        BusinessRuleFromScratch businessRuleFromScratch = gson.fromJson(businessRuleJsonString, BusinessRuleFromScratch.class);

        return businessRuleFromScratch;
    }

    /**
     * Converts given String JSON definition to BusinessRuleFromTemplate object
     *
     * @param jsonDefinition Given String JSON definition
     * @return TemplateGroup object
     */
    public static BusinessRuleFromTemplate jsonToBusinessRuleFromTemplate(String jsonDefinition) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        BusinessRuleFromTemplate businessRuleFromTemplate = gson.fromJson(jsonDefinition, BusinessRuleFromTemplate.class);

        return businessRuleFromTemplate;
    }

    /**
     * Converts given String JSON definition to BusinessRuleFromScratch object
     *
     * @param jsonDefinition Given String JSON definition
     * @return TemplateGroup object
     */
    public static BusinessRuleFromScratch jsonToBusinessRuleFromScratch(String jsonDefinition) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        BusinessRuleFromScratch businessRuleFromScratch = gson.fromJson(jsonDefinition, BusinessRuleFromScratch.class);

        return businessRuleFromScratch;
    }

    /**
     * Checks whether a given TemplateGroup object has valid content
     * Validation criteria : //todo: Implement properly
     * - name is available
     * - uuid is available
     * - At least one ruleTemplate is available
     * - Each available RuleTemplate should be valid
     *
     * @param templateGroup
     * @throws TemplateManagerException
     */
    public static void validateTemplateGroup(TemplateGroup templateGroup) throws TemplateManagerException {
        try {
            if (templateGroup.getName() == null) {
                throw new TemplateManagerException("Invalid TemplateGroup configuration file found - TemplateGroup " +
                        "name  is null" +
                        " ");
            }
            if (templateGroup.getUuid() == null) {
                throw new TemplateManagerException("Invalid TemplateGroup configuration file found - UUID is null for" +
                        " templateGroup " + templateGroup.getName());
            }
            if (templateGroup.getRuleTemplates().size() == 0) {
                throw new TemplateManagerException("Invalid TemplateGroup configuration file found - No ruleTemplate" +
                        " configurations found for templateGroup ");
            }
            for (RuleTemplate ruleTemplate : templateGroup.getRuleTemplates()) {
                validateRuleTemplate(ruleTemplate);
            }
        } catch (NullPointerException e) {
            // Occurs when no value for a key is found
            throw new TemplateManagerException("A required value can not be found in the template group definition", e);
        }
    }

    /**
     * Checks whether a given RuleTemplate object has valid content
     * <p>
     * Validation Criteria : todo: confirm validation criteria for RuleTemplate
     * - name is available
     * - uuid is available
     * - instanceCount is either 'one' or 'many'
     * - type is either 'template', 'input' or 'output'
     * - Only one template available if type is 'input' or 'output'; otherwise at least one template available
     * - Validate each template
     * - Templated elements from the templates, should be specified in either properties or script
     * - Validate all properties
     *
     * @param ruleTemplate
     * @throws TemplateManagerException
     */
    public static void validateRuleTemplate(RuleTemplate ruleTemplate) throws TemplateManagerException {
        try {
            if (ruleTemplate.getName() == null) {
                throw new TemplateManagerException("Invalid rule template - Rule template name is null ");
            }
            if (ruleTemplate.getUuid() == null) {
                throw new TemplateManagerException("Invalid rule template - UUID is null for rule template : " +
                        ruleTemplate.getName());
            }
            if (ruleTemplate.getInstanceCount().equals(TemplateManagerConstants.INSTANCE_COUNT_ONE) ||
                    ruleTemplate.getInstanceCount().equals(TemplateManagerConstants.INSTANCE_COUNT_MANY)) {
                if (ruleTemplate.getType() == null) {
                    throw new TemplateManagerException("Invalid rule template - rule template type is null for rule template : " +
                            ruleTemplate.getUuid());
                }
            }
            if (!(ruleTemplate.getType().equals(TemplateManagerConstants.RULE_TEMPLATE_TYPE_TEMPLATE) ||
                    ruleTemplate.getType().equals(TemplateManagerConstants.RULE_TEMPLATE_TYPE_INPUT) ||
                    ruleTemplate.getType().equals(TemplateManagerConstants.RULE_TEMPLATE_TYPE_OUTPUT))) {
                throw new TemplateManagerException("Invalid rule template - invalid rule template type for rule template " +
                        "" + ruleTemplate.getUuid());
            }
            if (ruleTemplate.getType().equals(TemplateManagerConstants.RULE_TEMPLATE_TYPE_INPUT) ||
                    ruleTemplate.getType().equals(TemplateManagerConstants.RULE_TEMPLATE_TYPE_OUTPUT)) {
                if (ruleTemplate.getTemplates().size() != 1) {
                    throw new TemplateManagerException("Invalid rule template - there should be exactly one template for " +
                            ruleTemplate.getType() + " type rule template - " + ruleTemplate.getUuid());
                }
            } else {
                if (ruleTemplate.getTemplates().size() == 0) {
                    throw new TemplateManagerException("Invalid rule template - No templates found in " +
                            ruleTemplate.getType() + " type rule template - " + ruleTemplate.getUuid());
                }
            }
            for (Template template : ruleTemplate.getTemplates()) {
                validateTemplate(template, ruleTemplate.getType());
            }
        } catch (NullPointerException e) {
            // Occurs when no value for a key is found
            throw new TemplateManagerException("A required value can not be found in the template group definition", e);
        }

        // Validate whether all templated elements have replacements
        validatePropertyTemplatedElements(ruleTemplate);
    }

    /**
     * Checks whether all the templated elements of all the templates under the given rule template,
     * are having replacement values either in properties, or in script
     *
     * @param ruleTemplate
     * @throws TemplateManagerException
     */
    public static void validatePropertyTemplatedElements(RuleTemplate ruleTemplate) throws TemplateManagerException {
        // Get script with templated elements and replace with values given in the BusinessRule
        String scriptWithTemplatedElements = ruleTemplate.getScript();

        // To store name and default value of all the properties to replace
        HashMap<String, String> propertiesMap = new HashMap<String, String>();
        Map<String, RuleTemplateProperty> ruleTemplateProperties = ruleTemplate.getProperties();

        // Put each property's name and default value
        for (String propertyName : ruleTemplateProperties.keySet()) {
            propertiesMap.put(propertyName, ruleTemplateProperties.get(propertyName).getDefaultValue());
        }

        String runnableScript = TemplateManagerHelper.replaceRegex(scriptWithTemplatedElements, TemplateManagerConstants.TEMPLATED_ELEMENT_NAME_REGEX_PATTERN, propertiesMap);

        // Run the script to get all the contained variables
        Map<String, String> scriptGeneratedVariables = TemplateManagerHelper.getScriptGeneratedVariables(runnableScript);

        propertiesMap.putAll(scriptGeneratedVariables);

        // Validate each template for replacement value
        for (Template template : ruleTemplate.getTemplates()) {
            try {
                validateContentWithTemplatedElements(template.getContent(), propertiesMap);
            } catch (TemplateManagerException e) {
                throw new TemplateManagerException("Invalid template. All the templated elements are not having " +
                        "replacements", e);
            }
        }
    }

    /**
     * Checks whether all the templated elements of the given content has a replacement, in given replacements
     *
     * @param content
     * @param replacements
     */
    public static void validateContentWithTemplatedElements(String content, Map<String, String> replacements) throws TemplateManagerException {
        Pattern templatedElementNamePattern = Pattern.compile(TemplateManagerConstants.TEMPLATED_ELEMENT_NAME_REGEX_PATTERN);
        Matcher templatedElementMatcher = templatedElementNamePattern.matcher(content);
        while (templatedElementMatcher.find()) {
            // If there is no replacement available
            if (replacements.get(templatedElementMatcher.group(1)) == null) {
                throw new TemplateManagerException("No replacement found for '" + templatedElementMatcher.group(1) + "'");
            }
        }
    }

    /**
     * Checks whether a given Template is valid
     * <p>
     * Validation Criteria :
     * - type is available
     * - content is available
     * - type should be 'siddhiApp' ('gadget' and 'dashboard' are not considered for now)
     * - exposedStremDefinition available if ruleTemplateType is either 'input' or 'output', otherwise not available
     *
     * @param template
     * @param ruleTemplateType
     * @throws TemplateManagerException
     */
    public static void validateTemplate(Template template, String ruleTemplateType) throws TemplateManagerException {
        try {
            if (template.getType() == null) {
                throw new TemplateManagerException("Invalid template. Template type not found");
            }
            if (template.getContent() == null) {
                throw new TemplateManagerException("Invalid template. Content not found");
            }
            if (template.getContent().isEmpty()) {
                throw new TemplateManagerException("Invalid template. Content can not be empty");
            }

            // If ruleTemplate type 'input' or 'output'
            if (ruleTemplateType.equals(TemplateManagerConstants.RULE_TEMPLATE_TYPE_INPUT) ||
                    ruleTemplateType.equals(TemplateManagerConstants.RULE_TEMPLATE_TYPE_OUTPUT)) {
                if (template.getExposedStreamDefinition() == null) {
                    throw new TemplateManagerException("Invalid template. Exposed stream definition not found for " +
                            "template within a rule template of type " + ruleTemplateType);
                }
                if (!template.getType().equals(TemplateManagerConstants.TEMPLATE_TYPE_SIDDHI_APP)) {
                    throw new TemplateManagerException("Invalid template. " + template.getType() +
                            " is not a valid template type for a template within a rule template" +
                            "of type " + ruleTemplateType + ". Template type must be '" +
                            TemplateManagerConstants.TEMPLATE_TYPE_SIDDHI_APP + "'");
                }
            } else {
                // If ruleTemplate type 'template'
                ArrayList<String> validTemplateTypes = new ArrayList<String>() {{
                    add(TemplateManagerConstants.TEMPLATE_TYPE_SIDDHI_APP);
                    add(TemplateManagerConstants.TEMPLATE_TYPE_GADGET);
                    add(TemplateManagerConstants.TEMPLATE_TYPE_SIDDHI_APP);
                }};

                if (template.getExposedStreamDefinition() != null) {
                    throw new TemplateManagerException("Invalid template. Exposed stream definition should not exist for " +
                            "template within a rule template of type " + ruleTemplateType);
                }
                if (!validTemplateTypes.contains(template.getType())) {
                    // Only siddhiApps are there for now
                    throw new TemplateManagerException("Invalid template. " + template.getType() +
                            " is not a valid template type for a template within a rule template" +
                            "of type " + ruleTemplateType + ". Template type must be '" +
                            TemplateManagerConstants.TEMPLATE_TYPE_SIDDHI_APP + "'");
                }
            }
        } catch (NullPointerException e) {
            // Occurs when no value for a key is found
            throw new TemplateManagerException("A required value can not be found in the template group definition", e);
        }
    }

    /**
     * Generates UUID for the given Template todo: figure out the needs
     *
     * @param template
     * @return
     */
    public static String generateUUID(Template template) throws TemplateManagerException {
        // SiddhiApp Template
        if (template.getType().equals(TemplateManagerConstants.TEMPLATE_TYPE_SIDDHI_APP)) {
            return getSiddhiAppName(template);
        }
        // Other template types are not considered for now
        throw new TemplateManagerException("Invalid template type. Unable to generate UUID"); // todo: (Q) is this correct?
    }

    /**
     * Gives the name of the given Template, which is a SiddhiApp
     *
     * @param siddhiAppTemplate
     * @return
     * @throws TemplateManagerException
     */
    public static String getSiddhiAppName(Template siddhiAppTemplate) throws TemplateManagerException {
        // Content of the SiddhiApp
        String siddhiApp = siddhiAppTemplate.getContent();
        // Regex match and find name
        Pattern siddhiAppNamePattern = Pattern.compile(TemplateManagerConstants.SIDDHI_APP_NAME_REGEX_PATTERN);
        Matcher siddhiAppNameMatcher = siddhiAppNamePattern.matcher(siddhiApp);
        if (siddhiAppNameMatcher.find()) {
            return siddhiAppNameMatcher.group(1);
        }

        throw new TemplateManagerException("Invalid SiddhiApp Name Found");
    }

    /**
     * Generates UUID from the given values, entered for the BusinessRuleFromTemplate todo: figure out usages
     * todo: This will be only called after user's form values come from the API (Read below)
     * 1. User enters values (propertyName : givenValue)
     * 2. TemplateGroupName, and RuleTemplateName is already there
     * 3. A Map with above details will be given from the API, to the backend
     * 4. These details are combined and the UUID is got
     * 5. BR object is created with those entered values, + the uuid in the backend
     *
     * @param givenValuesForBusinessRule
     * @return
     */
    public static String generateUUID(Map<String, String> givenValuesForBusinessRule) {
        return UUID.nameUUIDFromBytes(givenValuesForBusinessRule.toString().getBytes()).toString();
    }

    /**
     * Generates UUID, which only contains lowercase and hyphens,
     * from a TemplateGroup name todo: RuleTemplate name
     *
     * @param nameWithSpaces
     * @return
     */
    public static String generateUUID(String nameWithSpaces) {
        return nameWithSpaces.toLowerCase().replace(' ', '-');
    }

    /**
     * Replaces values with the given regex pattern in a given string, with provided replacement values
     *
     * @param stringWithRegex
     * @param regexPatternString
     * @param replacementValues
     * @return
     */
    public static String replaceRegex(String stringWithRegex, String regexPatternString, Map<String, String> replacementValues) throws TemplateManagerException {
        StringBuffer replacedString = new StringBuffer();

        Pattern regexPattern = Pattern.compile(regexPatternString);
        Matcher regexMatcher = regexPattern.matcher(stringWithRegex);

        // When an element with regex is is found
        while (regexMatcher.find()) {
            String elementToReplace = regexMatcher.group(1);
            String elementReplacement = replacementValues.get(elementToReplace);
            // No replacement found in the given map
            if (elementReplacement == null) {
                throw new TemplateManagerException("No matching replacement found for the value - " + elementToReplace);
            }
            // Replace element with regex, with the found replacement
            regexMatcher.appendReplacement(replacedString, elementReplacement);
        }
        regexMatcher.appendTail(replacedString);

        return replacedString.toString();
    }

    /**
     * Creates a BusinessRule object from a map of entered values, and the recieved RuleTemplate
     *
     * @param ruleTemplate  todo: might not need this method
     * @param enteredValues
     * @return
     */
    public static BusinessRule createBusinessRuleFromTemplateDefinition(RuleTemplate ruleTemplate, Map<String, String> enteredValues) {
        // Values required for replacement. Values processed by the script will be added to this
        Map<String, String> valuesForReplacement = enteredValues;
        // Script with templated elements
        String scriptWithTemplatedElements = ruleTemplate.getScript();

        return null;
    }

    /**
     * Runs the script that is given as a string, and gives all the variables specified in the script
     *
     * @param script
     * @return
     * @throws TemplateManagerException
     */
    public static Map<String, String> getScriptGeneratedVariables(String script) throws TemplateManagerException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        ScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
        try {
            // Run script
            engine.eval(script);
            Map<String, Object> returnedScriptContextBindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

            // Store binding variable values returned as objects, as strings
            Map<String, String> variableValues = new HashMap<String, String>();
            for (String variableName : returnedScriptContextBindings.keySet()) {
                variableValues.put(variableName, returnedScriptContextBindings.get(variableName).toString());
            }

            return variableValues;
        } catch (ScriptException e) {
            throw new TemplateManagerException("Error running the script :\n" + script + '\n', e);
        }
    }
}
