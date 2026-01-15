package com.currenjin.apimocker.parser

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.OpenAPIV3Parser
import org.springframework.stereotype.Component

@Component
class OpenApiParser {

    fun parse(spec: String): ParsedSpec {
        val openApi = OpenAPIV3Parser().readContents(spec).openAPI
            ?: throw IllegalArgumentException("Invalid OpenAPI spec")

        val endpoints = openApi.paths?.flatMap { (path, pathItem) ->
            listOfNotNull(
                pathItem.get?.let { Endpoint("GET", path, extractResponseSchema(openApi, it)) },
                pathItem.post?.let { Endpoint("POST", path, extractResponseSchema(openApi, it)) },
                pathItem.put?.let { Endpoint("PUT", path, extractResponseSchema(openApi, it)) },
                pathItem.delete?.let { Endpoint("DELETE", path, extractResponseSchema(openApi, it)) },
            )
        } ?: emptyList()

        val schemas = openApi.components?.schemas?.map { (name, schema) ->
            SchemaInfo(
                name = name,
                properties = schema.properties?.map { (propName, propSchema) ->
                    PropertyInfo(
                        name = propName,
                        type = propSchema.type ?: "object",
                        format = propSchema.format
                    )
                } ?: emptyList()
            )
        } ?: emptyList()

        return ParsedSpec(endpoints, schemas)
    }

    private fun extractResponseSchema(openApi: OpenAPI, operation: io.swagger.v3.oas.models.Operation): String? {
        val response = operation.responses?.get("200") ?: return null
        val content = response.content?.get("application/json") ?: return null
        val schema = content.schema ?: return null

        return when {
            schema.`$ref` != null -> schema.`$ref`.substringAfterLast("/")
            schema.items?.`$ref` != null -> schema.items.`$ref`.substringAfterLast("/")
            else -> null
        }
    }
}

data class ParsedSpec(
    val endpoints: List<Endpoint>,
    val schemas: List<SchemaInfo>
)

data class Endpoint(
    val method: String,
    val path: String,
    val responseSchema: String?
)

data class SchemaInfo(
    val name: String,
    val properties: List<PropertyInfo>
)

data class PropertyInfo(
    val name: String,
    val type: String,
    val format: String?
)
