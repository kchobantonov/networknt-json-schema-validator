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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class MultipleOfValidator extends BaseJsonValidator implements JsonValidator {
    private static final Logger logger = LoggerFactory.getLogger(MultipleOfValidator.class);

    private double divisor = 0;

    public MultipleOfValidator(String schemaPath, JsonNode schemaNode, JsonSchema parentSchema, ValidationContext validationContext) {

        super(schemaPath, schemaNode, parentSchema, ValidatorTypeCode.MULTIPLE_OF, validationContext);
        if (schemaNode.isNumber()) {
            divisor = schemaNode.doubleValue();
        }

        parseErrorCode(getValidatorType().getErrorCodeKey());
    }

    public Set<ValidationMessage> validate(ExecutionContext executionContext, JsonNode node, JsonNode rootNode, String at) {
        debug(logger, node, rootNode, at);

        if (node.isNumber()) {
            double nodeValue = node.doubleValue();
            if (divisor != 0) {
                // convert to BigDecimal since double type is not accurate enough to do the division and multiple
                BigDecimal accurateDividend = node.isBigDecimal() ? node.decimalValue() : new BigDecimal(String.valueOf(nodeValue));
                BigDecimal accurateDivisor = new BigDecimal(String.valueOf(divisor));
                if (accurateDividend.divideAndRemainder(accurateDivisor)[1].abs().compareTo(BigDecimal.ZERO) > 0) {
                    return Collections.singleton(buildValidationMessage(at, "" + divisor));
                }
            }
        }

        return Collections.emptySet();
    }

}
