/*
 * Copyright (c) 2016 Network New Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.networknt.schema;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NotAllowedValidator extends BaseJsonValidator implements JsonValidator {
    private static final Logger logger = LoggerFactory.getLogger(NotAllowedValidator.class);

    private List<String> fieldNames = new ArrayList<String>();

    public NotAllowedValidator(String schemaPath, JsonNode schemaNode, JsonSchema parentSchema, ValidationContext validationContext) {

        super(schemaPath, schemaNode, parentSchema, ValidatorTypeCode.NOT_ALLOWED, validationContext);
        if (schemaNode.isArray()) {
            int size = schemaNode.size();
            for (int i = 0; i < size; i++) {
                fieldNames.add(schemaNode.get(i).asText());
            }
        }

        parseErrorCode(getValidatorType().getErrorCodeKey());
    }

    public Set<ValidationMessage> validate(ExecutionContext executionContext, JsonNode node, JsonNode rootNode, String at) {
        debug(logger, node, rootNode, at);

        Set<ValidationMessage> errors = new LinkedHashSet<ValidationMessage>();

        for (String fieldName : fieldNames) {
            JsonNode propertyNode = node.get(fieldName);

            if (propertyNode != null) {
                errors.add(buildValidationMessage(at, fieldName));
            }
        }

        return Collections.unmodifiableSet(errors);
    }

}
