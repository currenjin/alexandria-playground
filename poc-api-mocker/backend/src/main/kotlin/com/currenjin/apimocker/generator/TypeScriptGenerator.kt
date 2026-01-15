package com.currenjin.apimocker.generator

import com.currenjin.apimocker.parser.PropertyInfo
import com.currenjin.apimocker.parser.SchemaInfo
import org.springframework.stereotype.Component

@Component
class TypeScriptGenerator {

    fun generateInterface(schema: SchemaInfo): String {
        val properties = schema.properties.joinToString("\n") { prop ->
            "  ${prop.name}: ${toTypeScriptType(prop)};"
        }

        return """export interface ${schema.name} {
$properties
}
"""
    }

    fun generateAllInterfaces(schemas: List<SchemaInfo>): String {
        return schemas.joinToString("\n") { generateInterface(it) }
    }

    private fun toTypeScriptType(prop: PropertyInfo): String {
        return when (prop.type) {
            "string" -> "string"
            "integer", "number" -> "number"
            "boolean" -> "boolean"
            "array" -> "any[]"
            else -> "any"
        }
    }
}
