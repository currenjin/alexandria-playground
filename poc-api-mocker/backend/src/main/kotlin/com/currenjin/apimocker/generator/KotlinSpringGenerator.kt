package com.currenjin.apimocker.generator

import com.currenjin.apimocker.parser.Endpoint
import com.currenjin.apimocker.parser.PropertyInfo
import com.currenjin.apimocker.parser.SchemaInfo
import org.springframework.stereotype.Component

@Component
class KotlinSpringGenerator {

    fun generateController(endpoints: List<Endpoint>): String {
        val methods = endpoints.joinToString("\n\n") { endpoint ->
            val methodName = generateMethodName(endpoint)
            val returnType = endpoint.responseSchema?.let { "List<$it>" } ?: "Any"
            val annotation = when (endpoint.method) {
                "GET" -> "@GetMapping(\"${endpoint.path}\")"
                "POST" -> "@PostMapping(\"${endpoint.path}\")"
                "PUT" -> "@PutMapping(\"${endpoint.path}\")"
                "DELETE" -> "@DeleteMapping(\"${endpoint.path}\")"
                else -> "@GetMapping(\"${endpoint.path}\")"
            }

            """    $annotation
    fun $methodName(): $returnType {
        TODO("Implement ${endpoint.method} ${endpoint.path}")
    }"""
        }

        return """package com.example.api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ApiController {

$methods
}
"""
    }

    fun generateDto(schema: SchemaInfo): String {
        val properties = schema.properties.joinToString(",\n") { prop ->
            "    val ${prop.name}: ${toKotlinType(prop)}"
        }

        return """package com.example.api.dto

data class ${schema.name}(
$properties
)
"""
    }

    private fun generateMethodName(endpoint: Endpoint): String {
        val parts = endpoint.path.split("/").filter { it.isNotEmpty() && !it.startsWith("{") }
        val base = parts.joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
        return when (endpoint.method) {
            "GET" -> "get$base"
            "POST" -> "create$base"
            "PUT" -> "update$base"
            "DELETE" -> "delete$base"
            else -> "handle$base"
        }
    }

    private fun toKotlinType(prop: PropertyInfo): String {
        return when (prop.type) {
            "string" -> "String"
            "integer" -> if (prop.format == "int64") "Long" else "Int"
            "number" -> "Double"
            "boolean" -> "Boolean"
            "array" -> "List<Any>"
            else -> "Any"
        }
    }
}
